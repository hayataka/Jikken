package com.example.demo.login.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.dao.DataAccessException;

public class ErrorAspect {
	@AfterThrowing(value = "execution(* *..*..*(..)) + && (bean(*Controller) || bean(*Service) || bean(*Repository))", throwing = "ex")

	public void throwingNull(DataAccessException ex) {
		// 例外処理
		System.out.println("===========================");
		System.out.println("DataAccessExceptionが発生しました。: " + ex);
		System.out.println("===========================");
	}
}
