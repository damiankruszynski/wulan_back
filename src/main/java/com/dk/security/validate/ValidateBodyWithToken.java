package com.dk.security.validate;

import com.dk.core.domain.Profile;
import com.dk.core.exception.NoProfileException;
import com.dk.core.exception.NoUserException;
import com.dk.core.service.ProfileService;
import com.dk.security.exception.ProfileIdIsNotBelowsToThisUserIdException;
import com.dk.security.jwt.JwtUtils;
import com.dk.security.login.domain.User;
import com.dk.security.login.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Slf4j
public class ValidateBodyWithToken extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private ProfileService profileService;
    private UserRepository userRepository;

    @Autowired
    public ValidateBodyWithToken(JwtUtils jwtUtils, UserRepository userRepository, ProfileService profileService){
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest =  new CachedBodyHttpServletRequest(httpServletRequest);
        try{
            String jwt = jwtUtils.parseJwt(cachedBodyHttpServletRequest);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                Optional<User> user = userRepository.findByUsername(username);
                if(user.isPresent()){
                    validateProfileIdForThisUser(cachedBodyHttpServletRequest, user.get());
                }else{
                    throw new NoUserException();
                }
            }
            filterChain.doFilter(cachedBodyHttpServletRequest,httpServletResponse);
        }
        catch (ProfileIdIsNotBelowsToThisUserIdException | JSONException | NoProfileException e ) {
            log.error(e.getMessage());
            log.error(e.getStackTrace().toString());
            final ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(httpServletResponse.getWriter(), errorDetails);
        }
    }

    private void validateProfileIdForThisUser(HttpServletRequest request, User user)
            throws NoProfileException, ProfileIdIsNotBelowsToThisUserIdException, JSONException, IOException {
        String bodyRequest = IOUtils.toString(request.getReader());
        if(bodyRequest.contains("profileId")) {
            JSONObject bodyRequestJSON = new JSONObject(bodyRequest);
            String profileId = bodyRequestJSON.getString("profileId");
            Optional<Profile> profileOptional = profileService.getProfileById(Long.parseLong(profileId));
            if(profileOptional.isPresent()){
               List<Long> listOfIdProfiles = user.getProfileList().stream().map(profile -> profile.getId()).collect(Collectors.toList());
               if(!listOfIdProfiles.contains(Long.parseLong(profileId))){
                   throw new ProfileIdIsNotBelowsToThisUserIdException();
               }

            }else{
                throw new ProfileIdIsNotBelowsToThisUserIdException();
            }
        }
    }
}
