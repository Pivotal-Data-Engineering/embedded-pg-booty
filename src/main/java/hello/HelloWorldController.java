package hello;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

	private static final String template = "Hello, %s (%s)!";
	private final AtomicLong counter = new AtomicLong();
	private ApplicationContext context;
	private PostgresDB pgDb = null;

	@GetMapping("/hello-world")
	@ResponseBody
	public Greeting sayHello(@RequestParam(name = "name", required = false, defaultValue = "Stranger") String name) {
		if (null == pgDb) {
			System.out.println("Initializing Postgres instance ...");
			this.pgDb = context.getBean(PostgresDB.class);
		}
		Connection conn = pgDb.getConnection();
		String now = "(TBD)";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT now();");
			rs.next();
			Timestamp ts = rs.getTimestamp(1);
			if (ts != null) {
				now = ts.toString();
			}
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return new Greeting(counter.incrementAndGet(), String.format(template, name, now));
	}

	@Autowired
	public void context(ApplicationContext context) {
		this.context = context;
	}

}
