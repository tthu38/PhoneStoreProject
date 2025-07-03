package service;

    import jakarta.servlet.ServletContext;

    public class WebConfigLoader {
        private static ServletContext context;

        public static void init(ServletContext servletContext) {
            context = servletContext;
        }

        public static String getProperty(String key) {
            if (context != null) {
                return context.getInitParameter(key);
            }
            return null;
        }

        public static String getProperty(String key, String defaultValue) {
            String value = getProperty(key);
            return value != null ? value : defaultValue;
        }
    }
