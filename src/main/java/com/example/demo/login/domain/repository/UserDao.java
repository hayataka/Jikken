package com.example.demo.login.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.example.demo.login.domain.model.User;

public interface UserDao {
	// ポイント DataAccessException
	// Userテーブルの件数を取得
	public int count() throws DataAccessException;

	// Userテーブルにデータを一件insert
	public int insertOne(User user) throws DataAccessException;

	public User selectOne(String userId) throws DataAccessException;

	public List<User> selectMany() throws DataAccessException;

	public int updateOne(User user) throws DataAccessException;

	public int deleteOne(String userId) throws DataAccessException;

	public void userCsvOut() throws DataAccessException;

}
