package actions.github.utils;

import java.util.HashMap;
import java.util.Map;

public class ArgsUtil {
    public static Map<String, String> extractArgs(String[] args) {
        var parsedArgs = new HashMap<String, String>();
        for (String arg : args) {
            var keyVal = arg.split("=");
            parsedArgs.put(keyVal[0], keyVal[1]);
        }
        return parsedArgs;
    }
}
