package com.zj.learning.model.course;

import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewGroup;

import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

public class AnswerCardItem extends ItemSingle implements Parcelable {

	/** 问题id，目前可能没有 **/
	private int questionId;
	/** 问题类型 **/
	private int questionType = Question.TYPE_SINGLE_CHOICE;
	/** 问题所处集合的下标 **/
	private int index;
	/** 问题答案 **/
	private List<String> answers;
	/** 问题本身需要几个答案，如单选一个、多选多个 **/
	private int totalAnswersCount = 0;
	/** 是否答对 **/
	private boolean isRight = false;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getTotalAnswersCount() {
		return totalAnswersCount;
	}

	public void setTotalAnswersCount(int totalAnswersCount) {
		this.totalAnswersCount = totalAnswersCount;
	}

	public boolean isAnswerAll() {
		if (null == answers || answers.isEmpty()) {
			return false;
		}
		if (Question.TYPE_CLOSE == questionType) {// 简答
			return answers.size() >= totalAnswersCount;
		} else {// 其他
			return answers.size() > 0;
		}
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.newCellFromXml(context, R.layout.course_answer_card_item,
				root);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(index);
		dest.writeInt(questionId);
		dest.writeInt(questionType);
		dest.writeList(answers);
		dest.writeInt(isRight ? 1 : 0);
	}

	public static final Parcelable.Creator<AnswerCardItem> CREATOR = new Parcelable.Creator<AnswerCardItem>() {

		@SuppressWarnings("unchecked")
		@Override
		public AnswerCardItem createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			AnswerCardItem count = new AnswerCardItem();
			count.setIndex(source.readInt());
			count.setQuestionId(source.readInt());
			count.setQuestionType(source.readInt());
			count.setAnswers(source.readArrayList(String.class.getClassLoader()));
			count.setRight(source.readInt() == 1 ? true : false);
			return count;
		}

		@Override
		public AnswerCardItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AnswerCardItem[size];
		}
	};

}
