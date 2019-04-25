package database;

import java.math.BigInteger;

public class HashPassword {

	public HashPassword() {

	}
	
	private static long power_base(int power) {
		return (power == 0 ? 1 : 128 * power_base(--power));
	}

	public String getHashPassword(String password) {

		BigInteger hashedpassword = new BigInteger("0");

		for (int i = 0; i < password.length(); i++) {
			String addNumberStr = Long.toString((password.charAt(password.length() - 1 - i) * power_base(i)));
			BigInteger addNumber = new BigInteger(addNumberStr);
			hashedpassword = hashedpassword.add(addNumber);
		}
		
		long[] w = new long[4];
		BigInteger temp = hashedpassword;
		BigInteger divisor = new BigInteger("65521");
		for (int i = 0; i < 4; i++) {
			if(i != 0) {
				temp = temp.divide(divisor);
			}
			BigInteger temp2 = temp.mod(divisor);
			w[i] = temp2.longValue();
		}

		long result = (45912 * w[3] + 35511 * w[2] + 65169 * w[1] + 4625 * w[0]) % 65521;

		return Long.toString(result);
	}
	
}
