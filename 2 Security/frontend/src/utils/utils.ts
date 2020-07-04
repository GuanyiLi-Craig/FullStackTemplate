import Vue from "vue";

export class Utils {
  private static URL_PREFIX = "/api/";

  public static getLoginUrl(username: string, password: string): string {
    return (
      Utils.URL_PREFIX + `login?username=` + username + `&password=` + password
    );
  }

  public static getUrl(category: string, request: string): string {
    return (
      Utils.URL_PREFIX +
      (category.length === 0 ? "" : category + "/") +
      (request.length === 0 ? "" : request + "/")
    );
  }

  public static getHeader(_cookieKey: string) {
    return {
      withCredentials: true
    };
  }

  // cookie handler
  public static getCookie(key: string): string {
    return Vue.$cookies.isKey(key) ? Vue.$cookies.get(key) : "";
  }

  public static setCookie(key: string, value: string, ttl?: number) {
    Vue.$cookies.set(key, value, ttl ? ttl : 81400);
  }
}
