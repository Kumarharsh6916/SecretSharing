package com.SecretSharing.SecretSharing;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;


@SpringBootApplication
public class SecretSharing {

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the JSON input:");
		String jsonContent = scanner.nextLine();

		JSONObject json = new JSONObject(jsonContent);

		JSONObject keys = json.getJSONObject("keys");
		int n = keys.getInt("n");
		int k = keys.getInt("k");

		List<Point> points = new ArrayList<>();
		for (String key : json.keySet()) {
			if (key.equals("keys")) continue;

			JSONObject root = json.getJSONObject(key);
			int base = Integer.parseInt(root.getString("base"));
			BigInteger x = new BigInteger(key);
			BigInteger y = new BigInteger(root.getString("value"), base);

			points.add(new Point(x, y));
		}

		BigInteger secretC = findConstantTerm(points, k);
		System.out.println(secretC);
	}

	static class Point {
		BigInteger x, y;

		public Point(BigInteger x, BigInteger y) {
			this.x = x;
			this.y = y;
		}

	}

	public static BigInteger findConstantTerm(List<Point> points, int k) {
		BigInteger constantTerm = BigInteger.ZERO;

		for (int i = 0; i < k; i++) {
			Point p1 = points.get(i);
			BigInteger term = p1.y;

			for (int j = 0; j < k; j++) {
				if (i != j) {
					Point p2 = points.get(j);
					term = term.multiply(p2.x.negate()).divide(p1.x.subtract(p2.x));
				}
			}

			constantTerm = constantTerm.add(term);
		}
		return constantTerm;
	}
}