package zhijin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import net.sf.json.JSONObject;

@Path("/courseRes")
public class CourseRes {

	Connection conn;

	public CourseRes() {
		// 驱动
		String driver = "com.mysql.jdbc.Driver";
		// 地址 数据库名
		String url = "jdbc:mysql://localhost:3306/minicourse?useUnicode=true&characterEncoding=utf-8";
		// 用户名
		String user = "root";
		// 密码
		String password = "root";
		// 驱动
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Sorry,can`t find the Driver!");
			// 连接数据库
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	/**
	 * 保存进度
	 * 
	 * @param jsonParam
	 *            {"memberId":"wliting@zhijin.com","progressJson":[{"memberId":
	 *            "1@zhijin.com", "sectionId":"1", "sectionProgress":"30",
	 *            "sectionName":"第三章" }]}
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveCourseProgress")
	@Consumes("application/x-www-form-urlencoded")
	public String saveCourse(@FormParam("jsonParam") String jsonParam) {
		System.out.println("jsonParam:"+jsonParam);
		String result = null;
		int resultCode = -1;
		// 解析json格式的数据
		if (jsonParam != null && !"".equals(jsonParam)) {
			try {
				JSONObject ob = JSONObject.fromObject(jsonParam);
				CourseProgress cp = (CourseProgress) JsonUtil
						.getObjectJsonString(ob, CourseProgress.class);
				if (!conn.isClosed()) {
					if (isExistMemberId(cp.getMemberId())) {
						resultCode = updateCourseProgress(cp);
					} else {
						resultCode = saveCourseProgress(cp);
					}
				}
			} catch (SQLException e) {
				System.out.println("e:" + e.toString());
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("e:" + e.toString());
				e.printStackTrace();
			}
			if (resultCode <= 0) {
				JSONObject errorObj = new JSONObject();
				errorObj.element("respCode", "0");
				errorObj.element("errorMsg", "学习记录保存失败");
				result = errorObj.toString();
			} else {
				JSONObject successObj = new JSONObject();
				successObj.element("respCode", "1");
				result = successObj.toString();
			}
		} else {
			JSONObject ob = new JSONObject();
			ob.element("respCode", "-1");
			ob.element("errorMsg", "Json数据有问题");
			result = ob.toString();
		}
		System.out.println("reult:"+result);
		return result;
	}

	/**
	 * 获取课程进度
	 * 
	 * http://localhost:8080/mlearning/rest/courseRes/getCourseProgress/wlitin@zhijin.com
	 * 
	 * @return {"respCode":"1",
	 *         "result":{"memberId":"wliting@sina.com","progressJson":
	 *         "[{memberId:wliting,json:memberId:wliting},{memberId:wliting@zhijin.com,json:sss}]"
	 *         }}
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCourseProgress/{memberId}")
	public String getCourseProgress(@PathParam("memberId") String memberId) {
		String result = "";
		JSONObject ob = new JSONObject();
		CourseProgress cp = null;
		try {
			if (!conn.isClosed()) {
				Statement statement = conn.createStatement();
				// 执行
				String sql = "select * from courseprogress where memberId = '"
						+ memberId + "'";
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					cp = new CourseProgress();
					String mid = rs.getString("memberId");
					String sJson = rs.getString("progressJson");
					cp.setMemberId(mid);
					cp.setProgressJson(sJson);
				}
				close(rs, null, conn);
			}
		} catch (SQLException e) {
			System.out.println("SQLException:"+e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception:"+e.toString());
			e.printStackTrace();
		}
		if (cp != null) {
			String data = JsonUtil.getJsonObject(cp);
			ob.element("respCode", "1");
			ob.element("result", data);
			result = ob.toString();
		} else {
			ob.element("respCode", "0");
			ob.element("errorMsg", "没有该用户的学习记录");
			result = ob.toString();
		}
		return result;
	}

	/**
	 * 判断是否有某个人的学习记录
	 * 
	 * @param memberId
	 *            用户Id
	 * @return 是否存在该用户的记录
	 */
	private boolean isExistMemberId(String memberId) {
		try {
			if (!conn.isClosed()) {
				Statement statement = conn.createStatement();
				// 执行
				String sql = "select * from courseprogress where memberId = '"
						+ memberId + "'";
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					close(rs, null, null);
					return true;
				}
			}
		} catch (SQLException e) {
			System.out.println("error:" + e.toString());
			return false;
		}
		return false;
	}

	// 更新数据
	public int updateCourseProgress(CourseProgress cp) {
		String sql = "update courseprogress set progressJson=? where memberId=?";
		return update(sql, cp.getProgressJson(), cp.getMemberId());
	}

	// 增加数据
	public int saveCourseProgress(CourseProgress cp) {
		String sql = "insert into courseprogress values(?,?)";
		return update(sql, cp.getMemberId(), cp.getProgressJson());
	}

	/*
	 * 增加、修改、删除,的方法
	 * 
	 * obj: 可变参数列表
	 */
	public int update(String sql, Object... obj) {
		PreparedStatement ps = null;
		int rows = 0;
		try {
			if (!conn.isClosed()) {
				// 创建PreparedStatement对象
				ps = conn.prepareStatement(sql);
				// 为查询语句设置参数
				setParameter(ps, obj);
				// 获得受影响的行数
				rows = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			close(null, ps, conn);
		}
		return rows;
	}

	/*
	 * 为预编译对象设置参数
	 */
	public void setParameter(PreparedStatement ps, Object... obj)
			throws SQLException {
		if (obj != null && obj.length > 0) {
			// 循环设置参数
			for (int i = 0; i < obj.length; i++) {
				ps.setObject(i + 1, obj[i]);
			}
		}
	}

	/*
	 * 关闭数据库连接，注意关闭的顺序
	 */
	public void close(ResultSet rs, PreparedStatement ps, Connection conn) {
		// 注意：最后打开的最先关闭
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("关闭ResultSet失败" + e.toString());
			}
		}
		if (ps != null) {
			try {
				ps.close();
				ps = null;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("关闭PreparedStatement失败" + e.toString());
			}
		}
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("关闭Connection失败" + e.toString());
			}
		}
	}

}
