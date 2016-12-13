package com.dreamdesigner.library.Utils;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ���ݸ�ʽ�ж�
 * 
 * @author Administrator
 * 
 */
public class InputFormat {
	/**
	 * �ж��ֻ���ʽ
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * �ж�email��ʽ�Ƿ���ȷ
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * �ж��Ƿ�ȫ������
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * ������֤������ʽ,���������������������Ļ��ͷ�ĳ���
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isPlateNumber(String platenumber) {
		String str = "^[\u4e00-\u9fa5_A-Z]{1}[A-Z]{1}[A-Z_0-9]{5}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(platenumber);
		return m.matches();
	}

	/**
	 * ������֤
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isChineseName(String name) {
		String str = ("^[\u4e00-\u9fa5]*$");
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(name);
		return m.matches(); // trueΪȫ���Ǻ��֣�������false
	}

	/**
	 * ���ܺţ�ֻ���Ǵ�д��ĸ���������
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isVIN(String name) {
		String str = ("^[A-Z0-9]+$");
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(name);
		return m.matches(); // trueΪȫ����ȷ��������false
	}

	/**
	 * ��֤���֤
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isCarDID(String name) {
		String str = ("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(name);
		return m.matches(); // trueΪȫ����ȷ��������false
	}

	/**
	 * Сд��ĸ�Զ�ת��Ϊ��д
	 * 
	 * @param val
	 * @return
	 */
	public static void convertCapitalize(final EditText view) {
		view.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@SuppressLint("DefaultLocale")
			@Override
			public void afterTextChanged(Editable arg0) {
				try {
					final String temp = arg0.toString();
					int length = temp.length();
					for (int i = 0; i < length; i++) {
						// String tem = temp.substring(i, i + 1);
						char c = temp.charAt(i);
						// �����ĸ��Сдִ���������
						if (Character.isLowerCase(c)) {
							// char[] temC = tem.toCharArray();
							// int mid = temC[0];
							int mid = c;
							if (mid >= 97 && mid <= 122) {
								// Сд��ĸ
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										view.setText(temp.toUpperCase());
										view.setSelection(temp.length());// �������������ĩβ
									}
								}, 200);

							}
						}
					}
				} catch (Exception e) {
					e.getMessage();
				}
			}
		});
	}

	/**
	 * ʵʱ�����ַ����ж�
	 * 
	 * @param val
	 * @return
	 */
	public static void monitorWordSum(final EditText view, final TextView text,
			final int sum, final Context context) {
		view.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int editStart;
			private int editEnd;
			private boolean isChange = false;

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				temp = arg0;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@SuppressLint("DefaultLocale")
			@Override
			public void afterTextChanged(Editable arg0) {
				try {
					editStart = view.getSelectionStart();
					editEnd = view.getSelectionEnd();
					text.setText(temp.length() + "/" + sum);
					if (temp.length() > sum) {
						Toast.makeText(context, "���������Ѿ�����" + sum + "�֣�",
								Toast.LENGTH_SHORT).show();
						arg0.delete(editStart - 1, editEnd);
						int tempSelection = editStart;
						view.setText(arg0);
						view.setSelection(tempSelection);
						view.setSelection(temp.length());// �������������ĩβ
					}
				} catch (Exception e) {
					e.getMessage();
				}
			}
		});
	}

	/**
	 * EditView To TextView
	 * 
	 * @param val
	 * @return
	 */
	public static void TextForTextView(final EditText view,
			final TextView text, final Context context) {
		view.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@SuppressLint("DefaultLocale")
			@Override
			public void afterTextChanged(Editable arg0) {
				try {
					text.setText(arg0);
				} catch (Exception e) {
					e.getMessage();
				}
			}
		});
	}

	/**
	 * ��ʽ�����ȣ�������λС��
	 * 
	 * @param f
	 * @return
	 */
	public static double FormatLongitude(double f) {
		try {
			DecimalFormat df = new DecimalFormat("#.000000");
			return Double.parseDouble(df.format(f));
		} catch (Exception e) {
			return f;
		}
	}

	/**
	 * ��ʽ��γ�ȣ�������λС��
	 * 
	 * @param f
	 * @return
	 */
	public static double FormatLatitude(double f) {
		try {
			DecimalFormat df = new DecimalFormat("#.00000");
			return Double.parseDouble(df.format(f));
		} catch (Exception e) {
			return f;
		}
	}

	/**
	 * �ж��Ƿ���mac��ַ
	 * 
	 * @param mac
	 * @return
	 */
	public static boolean IsMac(String mac) {
		String str = ("^[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}$");
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(mac);
		return m.matches(); // trueΪ�ǣ�������false
	}

	/**
	 * MD5ת��
	 * 
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * ����Gps����
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double GpsDistens(double lng1, double lat1, double lng2,
			double lat2) {
		double result = 6370996.81 * Math.acos(Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180)
				* Math.cos(lng1 * Math.PI / 180 - lng2 * Math.PI / 180)
				+ Math.sin(lat1 * Math.PI / 180)
				* Math.sin(lat2 * Math.PI / 180));

		return result;
	}

	/**
	 * ��ֹ����
	 * 
	 * @param editText
	 */
	public static void ForbidEdit(EditText editText) {
		editText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				EditText edit = (EditText) v;
				// ��סEditText��InputType������password
				int inType = edit.getInputType(); // backup
													// the
				// input
				// type
				edit.setInputType(InputType.TYPE_NULL); // disable
				// soft
				// input
				edit.onTouchEvent(event); // call native
											// handler
				edit.setInputType(inType); // restore input
											// type
				edit.setSelection(edit.getText().length());
				return true;

			}
		});
	}
}
