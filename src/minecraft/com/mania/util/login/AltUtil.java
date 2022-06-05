package com.mania.util.login;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import com.mania.MCHook;
import com.mania.Mania;
import com.mania.util.RandomUtil;
import net.minecraft.util.Session;

public class AltUtil implements MCHook {
	
	private static final YggdrasilUserAuthentication authServise = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, Mania.name).createUserAuthentication(Agent.MINECRAFT);
	
	public static void loginCracked(String username) {
		mc.setSession(new Session(username, "", "", "mojang"));
	}
	
	public static void loginCracked() {
		final String username = String.format("%s%s", Mania.name, RandomUtil.nextString(RandomUtil.nextInt(5, 10)));
		System.out.println(username);
		mc.setSession(new Session(username, "", "", "mojang"));
	}
	
	public static boolean loginMojang(final String mail, final String passworld) {
		authServise.setUsername(mail);
		authServise.setPassword(passworld);
		try {
			authServise.logIn();
			return true;
		} catch (AuthenticationException e) {
			return false;
		}
	}

}
