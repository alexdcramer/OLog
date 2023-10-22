package net.oijon.olog.info;

//last edit: 10/22/23 -N3

/**
 * A class to get the version information of the current build
 * @author alex
 *
 */
public class Info {

	private static String versionNum = generateVersionNum();
	private static String fullVersion = "OLog - v" + versionNum;
	
	private static String generateVersionNum() {
		return "1.0.0";
	}
	
	/**
	 * Gets the current version of Oijon Utils, for example "Oijon Utils - v1.2.0"
	 * @return The current version of Oijon Utils
	 */
	public static String getVersion() {
		return fullVersion;
	}
	
	/**
	 * Gets the version number of Oijon Utils, for example "1.1.1"
	 * @return The current version number of Oijon Utils
	 */
	public static String getVersionNum() {
		return versionNum;
	}
	
}
