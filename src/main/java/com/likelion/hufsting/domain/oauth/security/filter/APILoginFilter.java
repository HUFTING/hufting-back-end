package com.likelion.hufsting.domain.oauth.security.filter;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    public APILoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if (request.getMethod().equalsIgnoreCase("GET")) {
            log.info("GET METHOD FORBIDDEN");
            return null;
        }

        Map<String, String> jsonData = parseRequestJSON(request);

        log.info(jsonData);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                jsonData.get("mid"),
                jsonData.get("mpw"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }



    private Map<String ,String> parseRequestJSON(HttpServletRequest request) {

        //JSON 데이터를 parsing해서 mid,mpw를 Map처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader,Map.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
