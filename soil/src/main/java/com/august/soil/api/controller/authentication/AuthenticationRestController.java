package com.august.soil.api.controller.authentication;

import com.august.soil.api.controller.ApiResult;
import com.august.soil.api.error.UnauthorizedException;
import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.SnsType;
import com.august.soil.api.model.user.User;
import com.august.soil.api.security.AuthenticationResult;
import com.august.soil.api.security.JwtAuthenticationToken;
import com.august.soil.api.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static com.august.soil.api.controller.ApiResult.*;

/**
 * {@link org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter}에 대응되는 역할을 한다.
 * HTTP Request-Body 에서 로그인 파라미터(email, password)를 추출하고 로그인 처리를
 * {@link AuthenticationManager}로 위임한다.
 * 실제 구현 클래스는 {@link org.springframework.security.authentication.ProviderManager}이다. 이 클래스는
 * {@link com.august.soil.api.security.JwtAuthenticationProvider}를 포함하고 있다.
 * ({@link com.august.soil.api.configure.WebSecurityConfigure} 57라인)
 *
 * 로그인을 성공하면 User 정보와 JWT 값을 포함하는 {@link AuthenticationResultDto}를 반환한다.
 */

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "사용자 인증 APIs")
public class AuthenticationRestController {
  
  private final AuthenticationManager authenticationManager;
  
  private final UserService userService;
  
  @GetMapping("/{naverAccessToken}")
  @ApiOperation(value = "사용자 로그인(토큰 필요 없음)")
  public ApiResult<?> authentication(
    @ApiParam(value = "네이버에서 발급받은 접근 토큰(AccessToken)") @PathVariable(value = "naverAccessToken") String naverAccessToken,
    HttpServletResponse response
  ) throws UnauthorizedException, ParseException {
    try {
      if (naverAccessToken.equals("")) {
        return ERROR("Unauthorized", HttpStatus.UNAUTHORIZED);
      }
      
      // 접근토큰으로 네이버 회원 정보 요청
      String userProfileResponse = getUserProfile(naverAccessToken);
  
      // json 데이터에서 회원 정보 파싱
      JSONObject parseUserProfile = parseUserProfile(userProfileResponse);
      String userEmail = getUserInfo(parseUserProfile, "email");
      String username = getUserInfo(parseUserProfile, "name");
      Email email = new Email(userEmail);
      if (userService.findByEmail(email).isEmpty())
        userService.join(new User(SnsType.NAVER, username, email));
  
      String resultCode = getResultCode(userProfileResponse);
      JwtAuthenticationToken authToken = new JwtAuthenticationToken(userEmail, resultCode);
      Authentication authentication = authenticationManager.authenticate(authToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return OK(
        new AuthenticationResultDto((AuthenticationResult) authentication.getDetails())
      );
    } catch (AuthenticationException e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }
  
  private String getResultCode(String userProfileResponse) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject jsonObject = (JSONObject) parser.parse(userProfileResponse);
    return jsonObject.get("resultcode").toString();
  }
  
  private String getUserInfo(JSONObject userProfile, String field) {
    return userProfile.get(field).toString();
  }
  
  private JSONObject parseUserProfile(String userProfileResponse) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject jsonObject = (JSONObject) parser.parse(userProfileResponse);
    JSONObject userProfile = (JSONObject) jsonObject.get("response");
    return userProfile;
  }
  
  private String getUserProfile(String accessToken) {
    String header = "Bearer " + accessToken;
  
    String apiURL = "https://openapi.naver.com/v1/nid/me";
  
    Map<String, String> requestHeaders = new HashMap<>();
    requestHeaders.put("Authorization", header);
    String responseBody = get(apiURL, requestHeaders);
    return responseBody;
  }
  
  private static String get(String apiURL, Map<String, String> requestHeaders) {
    HttpURLConnection con = connect(apiURL);
    try {
      con.setRequestMethod("GET");
      for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
        con.setRequestProperty(header.getKey(), header.getValue());
      }
  
      int responseCode = con.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
        return readBody(con.getInputStream());
      } else { // 에러 발생
        return readBody(con.getInputStream());
      }
    } catch (IOException e) {
      throw new RuntimeException("API 요청과 응답 실패", e);
    } finally {
      con.disconnect();
    }
  }
  
  private static String readBody(InputStream body) {
    InputStreamReader streamReader = new InputStreamReader(body);
    try (BufferedReader lineReader = new BufferedReader(streamReader)) {
      StringBuilder responseBody = new StringBuilder();
      
      String line;
      while ((line = lineReader.readLine()) != null) {
        responseBody.append(line);
      }
  
      return responseBody.toString();
    } catch (IOException e) {
      throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
    }
  }
  
  private static HttpURLConnection connect(String apiURL) {
    try {
      URL url = new URL(apiURL);
      return (HttpURLConnection)url.openConnection();
    } catch (MalformedURLException e) {
      throw new RuntimeException("API URL이 잘못되었습니다. : " + apiURL, e);
    } catch (IOException e) {
      throw new RuntimeException("연결이 실패했습니다. : " + apiURL, e);
    }
  }
}
