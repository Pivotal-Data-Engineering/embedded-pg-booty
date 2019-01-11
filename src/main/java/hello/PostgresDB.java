package hello;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

/*
 * Ref. https://www.logicbig.com/tutorials/spring-framework/spring-boot/destruction-callback.html
 * https://github.com/yandex-qatools/postgresql-embedded#howto
 */
@Component
public class PostgresDB {

	private static EmbeddedPostgres postgres = null;
	private static String pgUrl = null;

	@PostConstruct
	public void init() {
		System.out.println("PostgresDB: init()");
		if (null == postgres) {
			postgres = new EmbeddedPostgres(Version.Main.V9_6);
			try {
				pgUrl = postgres.start("localhost", 35432, "dbName", "userName", "password");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Connection getConnection() {
		System.out.println("PostgresDB: getConnection()");
		Connection con = null;
		try {
			con = DriverManager.getConnection(pgUrl);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return con;
	}

	@PreDestroy
	public void destroy() {
		System.out.println("PostgresDB: destroy() -- stopping Postgres");
		if (postgres != null) {
			postgres.stop();
			postgres = null;
		}
	}

}
