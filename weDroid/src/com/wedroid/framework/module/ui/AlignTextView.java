package com.wedroid.framework.module.ui;

import java.util.ArrayList;

import com.wedroid.framework.common.TextUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import com.wedroid.framework.common.WedroidLog;
import android.widget.TextView;

public class AlignTextView extends TextView {

	private String TAG = "BaikeTextView";

	public Context mContext = null;

	public Paint mPaint = null;

	public int mTextHeight = 1080;

	public int mBaikeTextHeight = 0;

	public int mTextWidth = 1920;

	public String mText = "";

	public float mLineSpace = 20;

	public int mOffset = -2;

	public float mTextSize = 0;

	public int mTextColor = 0xffbbbbbb;

	public int mFontHeight = 0;

	public int mPaddingLeft = 0;

	public int mPaddingRight = 0;

	public AlignTextView(Context context, AttributeSet set) {
		this(context,set,0);

		this.mContext = context;

		mPaint = new Paint();

		mPaint.setAntiAlias(true);

		
	}

	public AlignTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
	}

	public AlignTextView(Context context) {
		this(context,null,0);
	}

	public float getmLineSpace() {
		return mLineSpace;
	}

	public void setmLineSpace(float mLineSpace) {
		this.mLineSpace = mLineSpace;
	}

	boolean first = true;

	public void isFirst(boolean first){
		this.first = first;
	}
	private ArrayList<LinePar> tempLineArray;
	@Override
	protected void onDraw(Canvas canvas) {

		mTextWidth = this.getWidth();

		setmTextHeight(this.getHeight());

		mText = this.getText().toString().trim();

		mText = getTextString(mContext, mText);

		if (mText == null || mText.equals("") == true) {

			return;

		}

		WedroidLog.i(TAG, "mTextStr: " + mText + "");

		mTextSize = this.getTextSize();

		mFontHeight = (int) mTextSize;

		mPaddingLeft = this.getPaddingLeft();

		mPaddingRight = this.getPaddingRight();

		mTextColor = this.getCurrentTextColor();

		mPaint.setTextSize(mTextSize);

		mPaint.setColor(mTextColor);

		if(first){
			first = false;
			tempLineArray = getLineParList(mText);
			mBaikeTextHeight = (int) (tempLineArray.size() * (mLineSpace + mTextSize));
			setHeight(mBaikeTextHeight);
			requestLayout();
		}else{
			drawText(tempLineArray, mText, canvas);
		}

	}

	/*
	 * 
	 * Obtain the information of each row
	 */

	public ArrayList<LinePar> getLineParList(String mTextStr) {

		if (mTextStr == null || mTextStr.isEmpty() == true) {

			return null;

		}

		int tempStart = 0;

		int tempLineWidth = 0;

		int tempLineCount = 0;

		ArrayList<LinePar> tempLineArray = new ArrayList<LinePar>();

		// ���ַ����һ��ѭ��

		for (int i = 0; i < mTextStr.length(); i++) {

			char ch = mTextStr.charAt(i);

			String str = String.valueOf(ch);

			float strWidth = 0;

			if (str != null && str.isEmpty() == false) {

				strWidth = TextUtil.getWidthofString(str, mPaint);

			}

			/*
			 * 
			 * ����ǻ��з���һ�е���Ϣ�����б���
			 */

			if (ch == '\n' && tempStart != i) {

				tempLineCount++;

				addLinePar(tempStart, i, tempLineCount, 0, tempLineArray);

				if (i == (mTextStr.length() - 1)) {

					break;

				} else {

					tempStart = i + 1;

					tempLineWidth = 0;

				}

				continue;

			} else {

				tempLineWidth += Math.ceil(strWidth);

				if (tempLineWidth >= mTextWidth - mPaddingRight) {

					tempLineCount++;

					/*
					 * 
					 * �������ʱ�ġ���һ�е�һ���ַ�����жϣ�����ǡ��ɶԳ��ֱ�㡱���������
					 * 
					 * ����һ�е��ַ���������죬���߲�����
					 */

					if (TextUtil.isLeftPunctuation(ch) == true) {

						WedroidLog.i(TAG, "i: " + i + "");

						WedroidLog.i(TAG,

						"the char is the left half of the punctuation");

						WedroidLog.i(TAG, "str: " + str + " ");

						/*
						 * 
						 * if the char is the left half of the punctuation. Go
						 * 
						 * into the next line of the current character
						 */

						i--;

						float tempWordSpaceOffset = (float) (tempLineWidth

						- Math.ceil(strWidth) - mTextWidth)

						/ (float) (i - tempStart);

						addLinePar(tempStart, i, tempLineCount,

						tempWordSpaceOffset, tempLineArray);

					} else if (TextUtil.isRightPunctuation(ch) == true) {

						/*
						 * 
						 * �������ʱ�ġ���һ�е�һ���ַ�����жϣ�����ǡ��ɶԳ��ֱ�㡱���Ҳ���
						 */

						WedroidLog.i(TAG,

						"the char is the right half of the punctuation");

						WedroidLog.i(TAG, "str: " + str + " ");

						if (i == (mTextStr.length() - 1)) {

							addLinePar(tempStart, i, tempLineCount, 0,

							tempLineArray);

							break;

						} else {

							char nextChar = mTextStr.charAt(i + 1);

							if ((TextUtil.isHalfPunctuation(nextChar) == true || TextUtil

							.isPunctuation(nextChar) == true)

									&& TextUtil

									.isLeftPunctuation(nextChar) == false) {

								/*
								 * 
								 * �������ʱ�ġ���һ�е�һ���ַ�����жϣ�����ǡ��ɶԳ��ֱ�㡱���Ҳ���
								 * 
								 * 
								 * 
								 * ���ң�������һ���ַ��ǡ�Ӣ�ı�㡱�������ı�㡱�����Ҳ��㡱
								 * 
								 * 
								 * 
								 * ���?����������㶼������һ�н��л���
								 */

								String nextStr = String.valueOf(nextChar);

								float nextStrWidth = 0;

								if (nextStr != null

								&& nextStr.isEmpty() == false) {

									nextStrWidth = TextUtil

									.getWidthofString(nextStr, mPaint);

								}

								i++;

								float tempWordSpaceOffset = (float) (tempLineWidth

										+ Math.ceil(nextStrWidth) - mTextWidth)

										/ (float) (i - tempStart);

								addLinePar(tempStart, i, tempLineCount,

								tempWordSpaceOffset, tempLineArray);

							} else {

								/*
								 * 
								 * �������ʱ�ġ���һ�е�һ���ַ�����жϣ�����ǡ��ɶԳ��ֱ�㡱���Ҳ���
								 * 
								 * 
								 * 
								 * ���ң�������һ���ַ��ǡ�����㡱���Ǳ����ַ�
								 * 
								 * 
								 * 
								 * ���?ֻ���Ҳ��������һ�н��л���
								 */

								float tempWordSpaceOffset = (float) (tempLineWidth - mTextWidth)

										/ (float) (i - tempStart);

								addLinePar(tempStart, i, tempLineCount,

								tempWordSpaceOffset, tempLineArray);

							}

						}

					} else {

						/*
						 * 
						 * �����һ�еĵ�һ���ַ��ǡ��������ֵı�㡱�͡��Ǳ���ַ�
						 */

						/*
						 * 
						 * if the char is not the left And Right half of the
						 * 
						 * punctuation.
						 */

						if (TextUtil.isHalfPunctuation(ch) == true

						|| TextUtil.isPunctuation(ch) == true) {

							/*
							 * 
							 * �����һ�еĵ�һ���ַ��ǡ��������ֵı�㡱
							 * 
							 * ������һ�н��л���
							 */

							/*
							 * 
							 * If the current character is a punctuation mark,
							 * 
							 * on the end of the Bank
							 */

							float tempWordSpaceOffset = (float) (tempLineWidth - mTextWidth)

									/ (float) (i - tempStart);

							addLinePar(tempStart, i, tempLineCount,

							tempWordSpaceOffset, tempLineArray);

						} else {

							/*
							 * 
							 * �����һ�еĵ�һ���ַ��ǡ��Ǳ�㡱
							 */

							/*
							 * 
							 * If the current character is not a punctuation
							 */

							if (i >= 1) {

								char preChar = mTextStr.charAt(i - 1);

								if (TextUtil.isLeftPunctuation(preChar) == true) {

									/*
									 * 
									 * �����һ�еĵ�һ���ַ��ǡ��Ǳ�㡱
									 * 
									 * 
									 * 
									 * ��һ���ַ�(����β���ַ�)���������
									 * 
									 * 
									 * 
									 * ���?�����ַ�ȫ��������һ�н��л���
									 */

									String preStr = String.valueOf(preChar);

									float preStrWidth = 0;

									if (preStr != null

									&& preStr.isEmpty() == false) {

										preStrWidth = TextUtil

										.getWidthofString(preStr,

										mPaint);

									}

									WedroidLog.i(TAG,

									"the char is the left half of the punctuation");

									WedroidLog.i(TAG, "preChar: " + preChar + " ");

									i = i - 2;

									float tempWordSpaceOffset = (float) (tempLineWidth

											- Math.ceil(strWidth)

											- Math.ceil(preStrWidth) - mTextWidth)

											/ (float) (i - tempStart);

									addLinePar(tempStart, i, tempLineCount,

									tempWordSpaceOffset, tempLineArray);

								} else {

									/*
									 * 
									 * �����һ�еĵ�һ���ַ��ǡ��Ǳ�㡱
									 * 
									 * 
									 * 
									 * ��һ���ַ�(����β���ַ�)���ǡ�������㡱
									 * 
									 * 
									 * 
									 * ���?��һ�еĵ�һ���ַ������һ��(����������)
									 */

									i--;

									float tempWordSpaceOffset = (float) (tempLineWidth

											- Math.ceil(strWidth) - mTextWidth)

											/ (float) (i - tempStart);

									addLinePar(tempStart, i, tempLineCount,

									tempWordSpaceOffset, tempLineArray);

								}

							}

						}

					}

					if (i == (mTextStr.length() - 1)) {

						break;

					} else {

						tempStart = i + 1;

						tempLineWidth = 0;

					}

					continue;

				} else {

					if (i == (mTextStr.length() - 1)) {

						tempLineCount++;

						addLinePar(tempStart, i, tempLineCount, 0,

						tempLineArray);

						break;

					}

					continue;

				}

			}

		}

		return tempLineArray;

	}

	public void addLinePar(int start, int end, int lineCount,

	float wordSpaceOffset, ArrayList<LinePar> lineList) {

		if (lineList != null) {

			LinePar linePar = new LinePar();

			linePar.setLineCount(lineCount);

			linePar.setStart(start);

			linePar.setEnd(end);

			linePar.setWordSpaceOffset(wordSpaceOffset);

			lineList.add(linePar);

		}

	}

	public void drawText(ArrayList<LinePar> tempLineArray, String mTextStr,

	Canvas canvas) {

		if (tempLineArray == null || canvas == null || mTextStr == null

		|| mTextStr.equals("") == true) {

			return;

		}

		int lineSize =  tempLineArray.size();
		for (int lineNum = 0; lineNum < lineSize; lineNum++) {

			LinePar linePar = tempLineArray.get(lineNum);

			int start = linePar.getStart();

			int end = linePar.getEnd();

			float width = linePar.getWordSpaceOffset();

			int lineCount = linePar.getLineCount();

			if (lineNum > 0 && lineNum == lineSize - 1) {
				mBaikeTextHeight = (int) (lineCount * (mLineSpace + mTextSize));
			}

			if (start > end || end > mTextStr.length() - 1) {

				continue;

			}

			float lineWidth = 0;

			for (int strNum = start; strNum <= end; strNum++) {

				char ch = mTextStr.charAt(strNum);

				String str = String.valueOf(ch);

				if (str == null || str.equals("") == true) {

					continue;

				}

				if (ch == '\n') {

					str = "";

				}

				if (strNum > end) {

					break;

				}

				if (strNum >= start && strNum <= end && lineCount >= 1) {

					canvas.drawText(str, mPaddingLeft + lineWidth, lineCount* mFontHeight - mOffset + (lineCount - 1)* mLineSpace, mPaint);

					lineWidth += TextUtil.getWidthofString(str, mPaint);

					lineWidth = lineWidth - width;

				}

			}

		}

	}

	public int getBaikeTextHeight() {

		return mBaikeTextHeight;

	}

	public String getTextString(Context mContext, String mText) {

		if (mContext != null && mText != null && mText.equals("") == false) {

			return TextUtil.replaceTABToSpace(mText);

		}

		return "";

	}

	public void setmTextHeight(int mTextHeight) {

		this.mTextHeight = mTextHeight;

	}

	public int getmTextHeight() {

		return mTextHeight;

	}

	public class LinePar {

		private int mStart;

		private int mEnd;

		private int mLineCount;

		private float mWordSpaceOffset;

		public void setStart(int mStart) {

			this.mStart = mStart;

		}

		public void setEnd(int mEnd) {

			this.mEnd = mEnd;

		}

		public void setLineCount(int count) {

			this.mLineCount = count;

		}

		public void setWordSpaceOffset(float mWordSpaceOffset) {

			this.mWordSpaceOffset = mWordSpaceOffset;

		}

		public int getStart() {

			return mStart;

		}

		public int getEnd() {

			return mEnd;

		}

		public int getLineCount() {

			return mLineCount;

		}

		public float getWordSpaceOffset() {

			return mWordSpaceOffset;

		}

	}

}
