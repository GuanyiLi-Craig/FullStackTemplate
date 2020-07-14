export class Utils {
  private static URL_PREFIX = "/api/";

  public static getSignupUrl(): string {
    return Utils.URL_PREFIX + `signup`;
  }

  public static getLoginUrl(): string {
    return Utils.URL_PREFIX + `login`;
  }

  public static getLogoutUrl(): string {
    return Utils.URL_PREFIX + `logout`;
  }

  public static getUrl(category: string, request: string): string {
    return (
      Utils.URL_PREFIX +
      (category.length === 0 ? "" : category + "/") +
      (request.length === 0 ? "" : request + "/")
    );
  }

  public static getHeaders() {
    const authz = localStorage.getItem("Authorization");
    return {
      headers: {
        Authorization: authz
      }
    }
  }
}
