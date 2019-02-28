package com.example.admin.select;

import android.animation.Animator;
import android.support.animation.DynamicAnimation;
import android.support.animation.FloatPropertyCompat;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static android.R.attr.animation;
import static android.R.attr.x;

public class MainActivity extends AppCompatActivity
{
	private RelativeLayout rlItem, rlNotSelected, rlSelected;
	private ImageView ivClip, imageview;
	private boolean isSelected;
	private FloatingActionButton fab, fabMove, fab1, fab2, fab3;
	private LinearLayout llFab;
	private SpringAnimation animationY, animationX, animMoveX, animMoveY;
	private float mXDiffInTouchPointAndViewTopLeftCorner;
	private float mYDiffInTouchPointAndViewTopLeftCorner;

	private float previousX, previousY;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		rlItem = findViewById(R.id.rlItem);
		rlNotSelected = findViewById(R.id.rlNotSelected);
		rlSelected = findViewById(R.id.rlSelected);
		ivClip = findViewById(R.id.ivClip);
		imageview = findViewById(R.id.imageview);

		fab = findViewById(R.id.fab);
		fabMove = findViewById(R.id.fabMove);
		fab1 = findViewById(R.id.fab1);
		fab2 = findViewById(R.id.fab2);
		fab3 = findViewById(R.id.fab3);

		llFab = findViewById(R.id.llFab);

		imageview.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SpringForce springForce = new SpringForce(0f);
				springForce.setStiffness(SpringForce.STIFFNESS_LOW);
				springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

				for(int i = 0; i < llFab.getChildCount(); i++)
				{
					View child = llFab.getChildAt(i);
					SpringAnimation rotationAnim;

					rotationAnim = new SpringAnimation(child,
							new FloatPropertyCompat<View>("rotation")
							{
								@Override
								public float getValue(View object)
								{
									return object.getRotation();
								}

								@Override
								public void setValue(View object, float value)
								{
									object.setRotation(value);
								}
							});

					rotationAnim.setSpring(springForce).setStartValue(-30).start();
				}

				SpringAnimation slideAnim = new SpringAnimation(llFab,
						new FloatPropertyCompat<View>("translationX") {

							@Override
							public float getValue(View view) {
								return view.getTranslationX();
							}

							@Override
							public void setValue(View view, float value) {
								view.setTranslationX(value);
							}
						}, 0);
				slideAnim.getSpring().setStiffness(500).setDampingRatio(0.4f);
				slideAnim.setStartValue(400).start();

			}
		});

		animationX = new SpringAnimation(fab, DynamicAnimation.TRANSLATION_Y, 0);
		animationX = new SpringAnimation(fab,
				new FloatPropertyCompat<View>("translationX") {

					@Override
					public float getValue(View view) {
						return view.getTranslationX();
					}

					@Override
					public void setValue(View view, float value) {
						view.setTranslationX(value);
					}
				});

		SpringForce forceX = new SpringForce(0f);
		forceX.setStiffness(SpringForce.STIFFNESS_LOW);
		forceX.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
		animationX.setSpring(forceX);

		animationY = new SpringAnimation(fab, DynamicAnimation.TRANSLATION_Y, 0);

		animationY = new SpringAnimation(fab,
				new FloatPropertyCompat<View>("translationY") {

					@Override
					public float getValue(View view) {
						return view.getTranslationY();
					}

					@Override
					public void setValue(View view, float value) {
						view.setTranslationY(value);
					}
				});

		SpringForce forceY = new SpringForce(0f);
		forceY.setStiffness(SpringForce.STIFFNESS_LOW);
		forceY.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
		animationY.setSpring(forceX);

		fab.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						mXDiffInTouchPointAndViewTopLeftCorner = event.getRawX() - v.getX();
						mYDiffInTouchPointAndViewTopLeftCorner = event.getRawY() - v.getY();

						animationX.cancel();
						animationY.cancel();
						break;

					case MotionEvent.ACTION_MOVE:
						float newTopLeftX = event.getRawX() - mXDiffInTouchPointAndViewTopLeftCorner;
						float newTopLeftY = event.getRawY() - mYDiffInTouchPointAndViewTopLeftCorner;
						fab.setX(newTopLeftX);
						fab.setY(newTopLeftY);
						break;

					case MotionEvent.ACTION_UP:
						animationX.start();
						animationY.start();
						break;
				}
				return true;
			}
		});
		/*animationX.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener()
		{
			@Override
			public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity)
			{
				fab.getTranslationX();
				fab.setTranslationX(value);
			}
		});

		animationY.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener()
		{
			@Override
			public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity)
			{
				fab.getTranslationY();
				fab.setTranslationY(value);
			}
		});*/

		rlItem.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!isSelected)
				{
					int x = (ivClip.getRight() + ivClip.getLeft())/2;
					int y = (ivClip.getTop() + ivClip.getBottom())/2;

					int startRadious = 0;
					int endRadious = (int) Math.hypot(rlItem.getWidth(), rlItem.getHeight());

					Animator anim = ViewAnimationUtils.createCircularReveal(rlSelected, x, y, startRadious, endRadious);
					rlSelected.setVisibility(View.VISIBLE);
					anim.start();
					isSelected = true;
				}
				else
				{
					int x = (ivClip.getRight() + ivClip.getLeft())/2;
					int y = (ivClip.getTop() + ivClip.getBottom())/2;

					int startRadious = (int) Math.max(rlItem.getWidth(), rlItem.getHeight());
					int endRadious = 0;

//			fab.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)));
//			fab.setImageResource(R.drawable.ic_add_black_24dp);

					Animator anim = ViewAnimationUtils.createCircularReveal(rlSelected, x, y, startRadious, endRadious);
					rlSelected.setVisibility(View.VISIBLE);
					anim.addListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animator) {

						}

						@Override
						public void onAnimationEnd(Animator animator) {
							rlSelected.setVisibility(View.GONE);
						}

						@Override
						public void onAnimationCancel(Animator animator) {

						}

						@Override
						public void onAnimationRepeat(Animator animator) {

						}
					});
					anim.start();
					isSelected = false;
				}
			}
		});

//		animMoveX = new SpringAnimation(fabMove, DynamicAnimation.TRANSLATION_X, 0);
		animMoveX = new SpringAnimation(fabMove,
				new FloatPropertyCompat<View>("translationX") {

					@Override
					public float getValue(View view) {
						return view.getTranslationX();
					}

					@Override
					public void setValue(View view, float value) {
						view.setTranslationX(value);
					}
				});
		animMoveX.setSpring(forceX);

//		animMoveY = new SpringAnimation(fabMove, DynamicAnimation.TRANSLATION_Y, 0);
		animMoveY = new SpringAnimation(fabMove,
				new FloatPropertyCompat<View>("translationY") {

					@Override
					public float getValue(View view) {
						return view.getTranslationX();
					}

					@Override
					public void setValue(View view, float value) {
						view.setTranslationX(value);
					}
				});
		animMoveY.setSpring(forceY);

		fabMove.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getActionMasked() == MotionEvent.ACTION_MOVE)
				{
						float x = event.getRawX() - previousX;
						float y = event.getRawY() - previousY;

						fabMove.setTranslationX(x + fabMove.getTranslationX());
						fabMove.setTranslationY(y + fabMove.getTranslationY());

				}
				previousX = event.getRawX();
				previousY = event.getRawY();
				return true;
			}
		});
	}
}
