package com.dantropov.medtest.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.animation.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dantropov.medtest.R
import com.dantropov.medtest.navigation.Screen
import com.dantropov.medtest.navigation.navigate
import com.dantropov.medtest.theme.MedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment() {

    private val viewModel: QuizViewModel by activityViewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.init(arguments)
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.Quiz)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
        return ComposeView(requireContext()).apply {
            setContent {
                MedTheme {
                    val state = viewModel.uiState.observeAsState().value ?: return@MedTheme
                    AnimatedContent(
                        targetState = state,
                        transitionSpec = {
                            fadeIn() with fadeOut()
                        }
                    ) { targetState ->
                        when (targetState) {
                            is QuizUiState.Ready -> QuizScreen(
                                R.string.practice,
                                targetState.medQuiz,
                                targetState.state,
                                { answer, correctOrder, state -> viewModel.answerClick(answer, correctOrder, state) }
                            ) { viewModel.onClick(targetState.state) }
                            is QuizUiState.Finish -> {}
                            is QuizUiState.Loading -> QuizScreenLoading(R.string.practice)
                            is QuizUiState.Error -> QuizScreenEmpty(R.string.practice)
                        }
                    }
                }
            }
        }
    }
}

//@AndroidEntryPoint
//class QuizFragment : Fragment() {
//
//    private val bindingHolder = ViewBindingHolder<FragmentQuizBinding>()
//    private val binding get() = bindingHolder.binding
//    private val viewModel: QuizViewModel by viewModels()
////    private val args: QuizFragmentArgs by navArgs()
//
//    private val optionsList: List<TextView> by lazy {
//        listOf(
//            binding.tvOption1,
//            binding.tvOption2,
//            binding.tvOption3,
//            binding.tvOption4,
//            binding.tvOption5
//        )
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View = bindingHolder.createView(viewLifecycleOwner) {
//        FragmentQuizBinding.inflate(inflater, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        viewModel.init(args.quizArg)
//        setupUi()
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.uiState.collect { uiState ->
//                    when (uiState) {
//                        is QuizUiState.Ready -> setupQuiz(uiState.medQuiz)
//                        is QuizUiState.CorrectAnswer -> chooseCorrectAnswer(uiState.correctOrder)
//                        is QuizUiState.WrongAnswer -> chooseWrongAnswer(uiState.wrongOrder, uiState.correctOrder)
//                        is QuizUiState.NavigateToNextQuestion -> navigateToNextQuestion(uiState.quizLevelData)
//                        is QuizUiState.Finish -> showFinishDialog(uiState.quizLevelData)
//                        else -> {}
//                    }
//                }
//            }
//        }
//    }
//
//    private fun chooseWrongAnswer(wrongOrder: Int, correctOrder: Int) {
//        val tvWrongAnswer = optionsList.getOrNull(wrongOrder) ?: return
////        tvWrongAnswer.setBackgroundColor(requireContext().getColor(R.color.colorWrong))
//        chooseCorrectAnswer(correctOrder)
//    }
//
//    private fun chooseCorrectAnswer(correctOrder: Int) {
//        val tvCorrect = optionsList.getOrNull(correctOrder) ?: return
//        TextViewAnimation.animateCorrectAnswer(tvCorrect)
//    }
//
//    private fun setupQuiz(medQuiz: MedQuiz) {
//        if (medQuiz.answers.size == 5) {
//            binding.tvQuestion.text = medQuiz.question
//            binding.tvOption1.text = medQuiz.answers[0].text
//            binding.tvOption2.text = medQuiz.answers[1].text
//            binding.tvOption3.text = medQuiz.answers[2].text
//            binding.tvOption4.text = medQuiz.answers[3].text
//            binding.tvOption5.text = medQuiz.answers[4].text
//        }
//    }
//
//    private fun setupUi() {
//        binding.tvOption1.setOnClickListener(parentClickListener { viewModel.itemChoose(0) })
//        binding.tvOption2.setOnClickListener(parentClickListener { viewModel.itemChoose(1) })
//        binding.tvOption3.setOnClickListener(parentClickListener { viewModel.itemChoose(2) })
//        binding.tvOption4.setOnClickListener(parentClickListener { viewModel.itemChoose(3) })
//        binding.tvOption5.setOnClickListener(parentClickListener { viewModel.itemChoose(4) })
////        binding.root.setOnTouchListener { v, event ->
////            when (event?.action) {
////                MotionEvent.ACTION_UP -> {
////                    viewModel.nextQuiz(args.quizArg)
////                    v.performClick()
////                }
////                else -> {}
////            }
////            v.onTouchEvent(event)
////        }
////        binding.root.setOnClickListener {
////            viewModel.nextQuiz(args.quizArg)
////        }
////        setupProgressBar()
//    }
//
//    private fun setupProgressBar() {
////        val quizData = args.quizArg
////        binding.progressBar.min = 1
////        binding.progressBar.max = quizData.questionsCount
////        binding.progressBar.setProgress(quizData.questionPosition, true)
////        binding.tvProgress.text = "${quizData.questionPosition}/${quizData.questionsCount} "
//    }
//
//    private fun navigateToNextQuestion(quizLevelData: QuizLevelData) {
////        findNavController().navigate(QuizFragmentDirections.actionQuizFragmentSelf(quizLevelData))
//    }
//
//    private fun showFinishDialog(data: QuizLevelData) {
//        val builder = AlertDialog.Builder(requireContext()).apply {
//            setTitle("Congratulations!")
//            setMessage("You finished current training: ${data.rightAnswersCount} / ${data.questionsCount} correct answers")
//            setPositiveButton(android.R.string.ok) { dialog, _ ->
//                finishQuiz()
//                dialog.dismiss()
//            }
//        }
//        builder.show()
//    }
//
//    private fun finishQuiz() {
//        findNavController().navigate(R.id.startFragment)
//    }
//
//    private fun parentClickListener(clickListener: () -> Unit): View.OnClickListener = View.OnClickListener { v ->
//        binding.root.performClick()
//        clickListener()
//    }
//}