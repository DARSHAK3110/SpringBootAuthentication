package com.training.authentication.security;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.training.authentication.entity.Log;
import com.training.authentication.entity.User;
import com.training.authentication.repository.LogRepository;
import com.training.authentication.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final UserRepository userRepository;
	private final LogRepository logRepository;
	private static final String SECRET_KEY = "368E615852A5885E69A591AD26B3711111111111111111111111111111111111111";

	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(getKeys())).build().parseClaimsJws(token)
				.getBody();
	}

	public String generateToken(UserDetails customUserDetails) {
		return generateToken(customUserDetails, new HashMap<>());
	}

	public String generateToken(UserDetails customUserDetails, Map<String, Object> claims) {
		Optional<User> user = userRepository
				.findByPhoneNumberAndDeletedAtIsNull(Long.parseLong(customUserDetails.getUsername()));
		Log log = new Log();
		if (user.isPresent()) {
			log.setUser(user.get());
		}
		logRepository.save(log);
		return Jwts.builder().setClaims(claims).setSubject(customUserDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.signWith(Keys.hmacShaKeyFor(getKeys()), SignatureAlgorithm.HS256).compact();
	}

	private byte[] getKeys() {
		return Decoders.BASE64.decode(SECRET_KEY).clone();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpirationDate(token).before(new Date(System.currentTimeMillis()));
	}

	private Date extractExpirationDate(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
