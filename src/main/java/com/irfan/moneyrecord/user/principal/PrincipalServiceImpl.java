package com.irfan.moneyrecord.user.principal;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class PrincipalServiceImpl implements PrincipalService {

	@Override
	public Object getAuthPrincipal() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth.getPrincipal() == null)
			throw new RuntimeException("Invalid Login");

		return auth.getPrincipal();
	}
}
