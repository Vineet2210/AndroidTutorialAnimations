package com.vineet.visithealthassesment.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.vineet.visithealthassesment.R
import com.vineet.visithealthassesment.databinding.FragmentStepAThonBinding
import com.vineet.visithealthassesment.presentation.IAnimationCallbackListener
import com.vineet.visithealthassesment.utils.AnimationUtils
import com.vineet.visithealthassesment.utils.UiUtils.hide
import com.vineet.visithealthassesment.utils.UiUtils.show
import kotlinx.coroutines.*
import kotlin.math.abs


class StepAThonFragment : Fragment(), IAnimationCallbackListener {

    private lateinit var binding: FragmentStepAThonBinding
    private val animationUtils by lazy {
        AnimationUtils()
    }
    private val gestureDetector by lazy {
        GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    private val imageTouchListener =
        OnTouchListener { _, event -> event?.let { gestureDetector.onTouchEvent(it) } ?: true }

    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepAThonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDotIndicators()
        initView()
    }

    override fun onAnimationStart() {
        manageBackgroundsAndText(position)
        updateDotIndicators(position)
        manageImageView(position)
        binding.ivSecondary.hide()
    }

    override fun onAnimationEnd() {
        manageBackgroundsAndText(position)
        updateDotIndicators(position)
        manageImageView(position)
    }

    /**
    function to manage the targetImage based on position
     **/
    private fun manageImageView(position: Int) {
        with(binding) {
            when (position) {
                FIRST_SCREEN -> {
                    ivPrimary.apply {
                        setImageResource(R.drawable.ic_trophy)
                        show()
                    }
                    tvNext.text = getString(R.string.next)
                }
                SECOND_SCREEN -> {
                    ivPrimary.apply {
                        setImageResource(R.drawable.ic_gift)
                        show()
                    }
                    tvNext.text = getString(R.string.next)

                }
                THIRD_SCREEN -> {
                    tvNext.text = getString(R.string.lets_go)
                    ivPrimary.hide()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        with(binding) {
            animationUtils.setListeners(this@StepAThonFragment)
            animationView.playAnimation()
            flMain.setOnTouchListener(imageTouchListener)
            tvNext.setOnClickListener {
                if (position < THIRD_SCREEN) {
                    position = position.plus(1)
                    animationUtils.animateView(binding.ivSecondary)
                }
            }
        }
    }


    /**
    function to manage the assets based on position changing on swipe gestures
     **/
    private fun manageBackgroundsAndText(position: Int) {
        with(binding) {
            when (position) {
                FIRST_SCREEN -> {
                    ivSecondary.setImageResource(R.drawable.ic_robot)
                    clParent.setBackgroundResource(R.drawable.bg_first_radial_gradient)
                    tvStepUp.text = getString(R.string.step_up_and_score)
                    tvJoinTheRace.text =
                        getString(R.string.join_the_race_lace_up_and_embrace_health)
                }
                SECOND_SCREEN -> {
                    ivSecondary.setImageResource(R.drawable.ic_trophy)
                    clParent.setBackgroundResource(R.drawable.bg_second_radial_gradient)
                    tvStepUp.text = getString(R.string.claim_the_throne)
                    tvJoinTheRace.text = getString(R.string.compete_with_collegues)
                }
                else -> {
                    ivSecondary.setImageResource(R.drawable.ic_gift)
                    clParent.setBackgroundResource(R.drawable.bg_third_radial_gradient)
                    tvStepUp.text = getString(R.string.score_big)
                    tvJoinTheRace.text = getString(R.string.victory_unlocks)
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                delay(150)
                ivSecondary.show()
            }
        }
    }

    /**
    setting up the bottom dot indicators
     **/
    private fun setupDotIndicators() {
        val dots =
            arrayOfNulls<View>(NO_OF_SCREENS)
        for (i in dots.indices) {
            dots[i] = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                    width = UNSELECTED_DOT
                    height = UNSELECTED_DOT
                }
                setBackgroundResource(R.drawable.bg_unselected_tab)
            }
            binding.dotsContainer.addView(dots[i])
        }
        dots[0]?.apply {
            setBackgroundResource(R.drawable.bg_selected_tab)
            layoutParams?.width = SELECTED_DOT
            requestLayout()
        }
    }

    /**
    updating the bottom dots indicator selection based on swipe gestures
     **/
    private fun updateDotIndicators(position: Int) {
        for (i in 0 until binding.dotsContainer.childCount) {
            val dot = binding.dotsContainer.getChildAt(i)
            dot.apply {
                if (i == position) {
                    setBackgroundResource(R.drawable.bg_selected_tab)
                    layoutParams.width = SELECTED_DOT
                    layoutParams.height = UNSELECTED_DOT
                } else {
                    setBackgroundResource(R.drawable.bg_unselected_tab)
                    layoutParams.width = UNSELECTED_DOT
                    layoutParams.height = UNSELECTED_DOT
                }
                requestLayout()
            }
        }
    }


    /**
    gesture detector in order to get callback of swipe left and swipe right
     **/
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val deltaX = e2.x - (e1?.x ?: 0f)
            val deltaY = e2.y - (e1?.y ?: 0f)
            if (abs(deltaX) > abs(deltaY)) {
                if (deltaX > 0) {
                    if (position > FIRST_SCREEN) {
                        position = position.minus(1)
                        animationUtils.reverseAnimateView(binding.ivPrimary)
                    }
                    return true
                } else if (deltaX < 0) {
                    if (position < THIRD_SCREEN) {
                        position = position.plus(1)
                        animationUtils.animateView(binding.ivSecondary)
                    }
                    return true
                }
            }
            return false
        }
    }

    companion object {
        const val UNSELECTED_DOT: Int = 15
        const val SELECTED_DOT: Int = 35
        const val NO_OF_SCREENS: Int = 3
        const val FIRST_SCREEN: Int = 0
        const val SECOND_SCREEN: Int = 1
        const val THIRD_SCREEN: Int = 2
    }

}