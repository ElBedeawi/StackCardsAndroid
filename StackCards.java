package com.benchmark.sukkarselfie.utilities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benchmark.sukkarselfie.DashboardActivity;
import com.benchmark.sukkarselfie.R;

import java.lang.ref.WeakReference;

/**
 * Created by Wagih on 7/22/2015.
 */
public class StackCards {

    private RelativeLayout cardsContainer;
    private int topCard, cardsNumber, cardHeightDP, cardDiffDP, swipeValueDP;
    private float cardScale = 0.02f;
    private int animationDuration = 250;
    private boolean canAnimate = true;
    private WeakReference<Activity> owningActivity;

    final float[] y1 = new float[1]; final float[] y2 = new float[1]; final float[] dy = new float[1];

    public StackCards(Activity owningActivity, RelativeLayout container, int cardHeightDP, int cardDiffDP,float cardScale, int animationDuration)
    {
        this.owningActivity = new WeakReference<>(owningActivity);
        this.cardsContainer = container;
        this.cardHeightDP = PxToDp(cardHeightDP);
        this.cardDiffDP = PxToDp(cardDiffDP);
        this.cardScale = cardScale;
        this.swipeValueDP = PxToDp(25);
        this.animationDuration = animationDuration;
        this.topCard = 1;
    }

    public void initCards(int cardsNumber, int resourceXml){

        this.cardsNumber = cardsNumber;
        for(int i = 0; i <= cardsNumber; i++)
        {
            float scaleValue = cardScale * ( cardsNumber - i );
            RelativeLayout stackcard  = (RelativeLayout) owningActivity.get().getLayoutInflater().inflate(resourceXml, null);

            stackcard.setScaleX( scaleValue );
            stackcard.setScaleY( scaleValue );

            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cardHeightDP);

            rlParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            stackcard.setLayoutParams(rlParams);
            stackcard.setGravity(Gravity.CENTER_HORIZONTAL);
            stackcard.setHorizontalGravity(Gravity.CENTER);

            TextView titleText = (TextView) stackcard.findViewById(R.id.card_title);
            titleText.setTextColor(titleText.getTextColors().withAlpha((int) (scaleValue * 255)));
            titleText.setText(titleText.getText().toString());

            Typeface font = Typeface.createFromAsset(owningActivity.get().getAssets(), "fonts/new_x_digital_tfb.ttf");

            TextView readingUnitTxtView = (TextView) stackcard.findViewById(R.id.reading_unit);
            readingUnitTxtView.setTypeface(font);

            TextView readingValueTxtView = (TextView) stackcard.findViewById(R.id.reading_value);
            readingValueTxtView.setTypeface(font);

            stackcard.setTranslationY( - ( cardsNumber - i ) * cardDiffDP );
            cardsContainer.addView(stackcard);
        }

        cardsContainer.setOnTouchListener(containerTouchListner());

    }

    private View.OnTouchListener containerTouchListner(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        y1[0] = event.getY();
                        return true;
                    case (MotionEvent.ACTION_UP): {
                        y2[0] = event.getY();
                        dy[0] = y2[0] - y1[0];
                        if(canAnimate) {
                            if (dy[0] > swipeValueDP) {
                                translateDownView();
                            } else if (dy[0] < -swipeValueDP) {
                                translateUpView();
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private void translateDownView(){
        if(topCard< cardsContainer.getChildCount() -1) {
            View card = cardsContainer.getChildAt(cardsContainer.getChildCount() - topCard - 1);
            Log.d("TOPCARD-D", String.valueOf(topCard));

            ObjectAnimator currentCardTranslateDown = ObjectAnimator.ofFloat(card, "Y", card.getY(), card.getY()+1000);
            currentCardTranslateDown.setDuration(animationDuration);

            AnimatorSet restCardAnimatiorSet = new AnimatorSet();
            int j =1;
            for(int i =topCard+1 ; i <cardsContainer.getChildCount();i++)
            {
                View nextCard = cardsContainer.getChildAt(cardsContainer.getChildCount() - i - 1);

                ObjectAnimator cardScaleXUp = ObjectAnimator.ofFloat(nextCard, "scaleX", nextCard.getScaleX(), nextCard.getScaleX() + cardScale);
                cardScaleXUp.setDuration(animationDuration);
                ObjectAnimator cardScaleYUp = ObjectAnimator.ofFloat(nextCard, "scaleY",nextCard.getScaleY(),nextCard.getScaleY()+cardScale);
                cardScaleYUp.setDuration(animationDuration);

                ObjectAnimator cardTranslateDown = ObjectAnimator.ofFloat(nextCard, "Y", nextCard.getY(), nextCard.getY()+ cardDiffDP);
                cardTranslateDown.setDuration(animationDuration);
                restCardAnimatiorSet.play(cardScaleXUp).with(cardScaleYUp).with(cardTranslateDown);
                j++;
            }
            restCardAnimatiorSet.setDuration(animationDuration);

            canAnimate = false;
            AnimatorSet fullAnimatorSet = new AnimatorSet();
            fullAnimatorSet.setDuration(animationDuration);
            fullAnimatorSet.play(currentCardTranslateDown).with(restCardAnimatiorSet);
            fullAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    canAnimate = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            fullAnimatorSet.start();
            topCard++;
        }
    }
    private void translateUpView(){
        if(topCard > 7)topCard=7;
        if(topCard > 0) {
            View card = cardsContainer.getChildAt(cardsContainer.getChildCount() - topCard);
            Log.d("TOPCARD-U", String.valueOf(topCard));

            ObjectAnimator currentCardTranslateDown = ObjectAnimator.ofFloat(card, "Y", card.getY(), card.getY()-1000);
            currentCardTranslateDown.setDuration(animationDuration);

            AnimatorSet restOfCardAnimatiorSet = new AnimatorSet();
            int j =1;
            for(int i =topCard ; i <cardsContainer.getChildCount();i++)
            {
                View nextCard = cardsContainer.getChildAt(cardsContainer.getChildCount() - i - 1);

                ObjectAnimator cardScaleXUp = ObjectAnimator.ofFloat(nextCard, "scaleX", nextCard.getScaleX(), nextCard.getScaleX() - cardScale);
                cardScaleXUp.setDuration(animationDuration);
                ObjectAnimator cardScaleYUp = ObjectAnimator.ofFloat(nextCard, "scaleY", nextCard.getScaleY(), nextCard.getScaleY() - cardScale);
                cardScaleYUp.setDuration(animationDuration);

                ObjectAnimator cardTranslateDown = ObjectAnimator.ofFloat(nextCard, "Y", nextCard.getY(), nextCard.getY()- cardDiffDP);
                cardTranslateDown.setDuration(animationDuration);
                restOfCardAnimatiorSet.play(cardScaleXUp).with(cardScaleYUp).with(cardTranslateDown);
                j++;
            }
            restOfCardAnimatiorSet.setDuration(animationDuration);

            canAnimate = false;
            AnimatorSet fullAnimatorSet = new AnimatorSet();
            fullAnimatorSet.setDuration(animationDuration);
            fullAnimatorSet.play(currentCardTranslateDown).with(restOfCardAnimatiorSet);
            fullAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    canAnimate = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            fullAnimatorSet.start();

            topCard--;
        }
    }


    //Helper Functions
    private int PxToDp(int px)
    {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                px,
                owningActivity.get().getResources().getDisplayMetrics()
        );
    }


}
