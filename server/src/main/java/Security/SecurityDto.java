package Security;


public class SecurityDto {
	
	
    public record Login(String login, String password) {

		public Object username() {
			// TODO Auto-generated method stub
			return login;
		}

    	
    	
    	
    }
}
