package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Repository("UserDaoJdbcImpl")
public class UserDaoJdbcImpl implements UserDao {

	@Autowired
	JdbcTemplate jdbc;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public int count() throws DataAccessException {
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_user", Integer.class);

		return count;
	}

	// Userテーブルにデータを1件insert
	@Override
	public int insertOne(User user) throws DataAccessException {
		// パスワード暗号化
		String password = passwordEncoder.encode(user.getPassword());

		// ユーザーテーブルに1件登録するSQL
		String sql = "INSERT INTO m_user(user_id,password,user_name,birthday,age,marriage,role)VALUES(?,?,?,?,?,?,?)";

		// 1件挿入するSQL
		int rowNumber = jdbc.update(
				"insert into m_user(user_id,password,user_name,birthday,age,marriage,role) values(?,?,?,?,?,?,?)",
				user.getUserId(), password, user.getUserName(), user.getBirthday(), user.getAge(), user.isMarriage(),
				user.getRole());

		return rowNumber;
	}

	@Override
	public User selectOne(String userId) throws DataAccessException {
		// 一件取得
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_user WHERE user_id=?", userId);
		// 結果返却用の変数
		User user = new User();

		// 取得したデータを結果返却用の変数にセットする
		user.setUserId((String) map.get("user_id"));
		user.setPassword((String) map.get("password"));
		user.setUserName((String) map.get("user_name"));
		user.setBirthday((Date) map.get("birthday"));
		user.setAge((Integer) map.get("age"));
		user.setMarriage((Boolean) map.get("marriage"));
		user.setRole((String) map.get("role"));

		return user;

	}

	@Override
	public List<User> selectMany() throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM m_user");
		List<User> userList = new ArrayList<>();

		for (Map<String, Object> map : getList) {
			User user = new User();

			user.setUserId((String) map.get("user_id"));
			user.setPassword((String) map.get("password"));
			user.setUserName((String) map.get("user_name"));
			user.setBirthday((Date) map.get("birthday"));
			user.setAge((Integer) map.get("age"));
			user.setMarriage((Boolean) map.get("marriage"));
			user.setRole((String) map.get("role"));
			// 結果返却用のListに追加
			userList.add(user);

		}
		return userList;
	}

	@Override
	public int updateOne(User user) throws DataAccessException {
		String password = passwordEncoder.encode(user.getPassword());
		// 1件更新
		int rowNumber = jdbc.update(
				"UPDATE m_user SET password=?,user_name=?, birthday=?, age=?,marriage=? WHERE user_id =?", password,
				user.getUserName(), user.getBirthday(), user.getAge(), user.isMarriage(), user.getUserId());

//		// トランザクション確認のため、わざと例外をthrowする。
//		if (rowNumber > 0) {
//			throw new DataAccessException("トランザクションテスト") {
//			};

		return rowNumber;
	}

	@Override
	public int deleteOne(String userId) throws DataAccessException {
		// 1件削除
		int rowNumber = jdbc.update("DELETE FROM m_user WHERE user_id=?", userId);

		return rowNumber;
	}

	@Override
	public void userCsvOut() throws DataAccessException {
		// m_userテーブルのデータを全件取得するSQl
		String sql = "SELECT * FROM m_user";
		UserRowCallbackHandler handler = new UserRowCallbackHandler();

		jdbc.query(sql, handler);
	}
}
