package org.letter.springboot.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 本机约束的环境变量, 运行参数 优先级 > 系统环境变量.
 * <pre>
 * | 状态 | 参数               | 备注
 * | --- | ------------------ | ------------------------------------------------------
 * | 保留 | 节点名             | -Dnode.name 传参方式
 * | 新增 | 节点IP             | -Dnode.ip 传参
 * | 新增 | 节点端口           | -Dnode.port 此端口为外部可访问的(区别于绑定端口)
 * | 新增 | 节点权重            | -Dnode.weight 传参
 * | 保留 | 专属ID             | -Dnode.exclusiveid 服务将通过此参数，判断是否注册为专属服务
 * | 新增 | 数据中心            | -Dserver.env
 * | 新增 | 集群名称            | -Dserver.cluster
 * | 新增 | AZ名称             | -Dserver.site (优先从本地配置获取，劣后从配置中心获取)
 * | 修改 | Zk地址             | -Dserver.zkaddr (优先从本地配置获取，劣后从配置中心获取)
 * | 新增 | 应用名称            | -Dserver.app (apollo中应用名称)
 * | 新增 | metaServer地址     | -Dserver.meta (apollo metaServer地址)
 * | 新增 | configServer地址   ｜ -Dserver.conf (apollo configServer地址)
 * </pre>
 *
 * @author letter
 **/
public final class Environments {

	private static final Map<String, String> VARS_MAP = new HashMap<>(32);
	private static final Map<String, String> VARS_CASE_INSENSITIVE = new HashMap<>(32);

	/**
	 * 节点名称
	 */
	public static final String NODE_NAME = "node.name";
	/**
	 * 节点IP
	 */
	public static final String NODE_IP = "node.ip";
	/**
	 * 节点端口
	 */
	public static final String NODE_PORT = "node.port";
	/**
	 * 节点权重
	 */
	public static final String NODE_WEIGHT = "node.weight";
	//所在 site(AZ)
	public static final String SERVER_SITE = "server.site";
	//所在 cluster
	public static final String SERVER_CLUSTER = "server.cluster";
	//所用 zk地址
	public static final String SERVER_ZKADDR = "server.zkaddr";
	//专属ID
	public static final String EXCLUSIVE_ID = "node.exclusiveid";
	//apollo 数据中心
	public static final String SERVER_ENV = "server.env";
	//apollo 服务对应的应用名称
	public static final String SERVER_APP = "server.app";
	//apollo metaServer 地址
	public static final String SERVER_META = "server.meta";
	//apollo configServer 地址
	public static final String SERVER_CONF = "server.conf";

	//apollo configServer 地址
	public static final String CONFIG_TYPE = "config.type";

	public static final String LOG_TYPE = "log.type";

	public static final String THREAD_TYPE = "thread.type";


	static {
		loadSystemVariables();
	}

	private Environments() {
	}

	/**
	 * 加载系统配置
	 */
	public static void loadSystemVariables() {
		for (Map.Entry<String, String> e : System.getenv().entrySet()) {
			VARS_MAP.put(e.getKey(), e.getValue());
			VARS_CASE_INSENSITIVE.put(
					e.getKey().toLowerCase(), e.getValue());
		}
		Properties properties = System.getProperties();
		for (String pk : properties.stringPropertyNames()) {
			VARS_MAP.put(pk, properties.getProperty(pk));
			VARS_CASE_INSENSITIVE.put(
					pk.toLowerCase(), properties.getProperty(pk));
		}
	}

	// testing purpose
	public static void clearSystemVariables() {
		VARS_MAP.clear();
		VARS_CASE_INSENSITIVE.clear();
	}

	/**
	 * 得到一个环境变量
	 */
	public static String getVar(String key) {
		return VARS_MAP.get(key);
	}

	/**
	 * 建议区分大小写 {@link #getVar(String)}
	 */
	public static String getVarCaseInsensitive(String key) {
		return VARS_CASE_INSENSITIVE.get(key.toLowerCase());
	}

	/**
	 * 读取 运行参数 node.name
	 */
	public static String getNodeName() {
		return getVarCaseInsensitive(NODE_NAME);
	}

	/**
	 * 读取 运行参数 node.ip
	 */
	public static String getNodeIp() {
		return getVarCaseInsensitive(NODE_IP);
	}

	/**
	 * 读取 运行参数 node.port
	 */
	public static String getNodePort() {
		return getVarCaseInsensitive(NODE_PORT);
	}

	/**
	 * 读取 运行参数 node.weight
	 */
	public static String getNodeWeight() {
		return getVarCaseInsensitive(NODE_WEIGHT);
	}

	/**
	 * 读取 运行参数 exclusiveid
	 */
	public static String getExclusiveId() {
		return getVarCaseInsensitive(EXCLUSIVE_ID);
	}

	/**
	 * 读取 运行参数 exclusiveid
	 */
	public static boolean isExclusiveId() {
		String excid = Environments.getExclusiveId();

		if (!StringUtils.isEmpty(excid) && !"0".equals(excid.trim())) {
			//专属集群
			return true;
		}
		return false;
	}

	/**
	 * 读取 运行参数 server.site
	 */
	public static String getServerSite() {
		return getVarCaseInsensitive(SERVER_SITE);
	}

	/**
	 * 读取 运行参数 server.env
	 */
	public static String getServerEnv() {
		return getVarCaseInsensitive(SERVER_ENV);
	}

	/**
	 * 读取 运行参数 server.cluster
	 */
	public static String getServerCluster() {
		return getVarCaseInsensitive(SERVER_CLUSTER);
	}

	/**
	 * 读取 运行参数 server.zkaddr
	 */
	public static String getServerZkaddr() {
		return getVarCaseInsensitive(SERVER_ZKADDR);
	}

	public static void resetSystemProperty(String key, String value) {
		VARS_MAP.put(key, value);
		VARS_CASE_INSENSITIVE.put(key.toLowerCase(), value);
	}
}
