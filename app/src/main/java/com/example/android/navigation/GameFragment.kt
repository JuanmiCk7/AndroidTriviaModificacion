/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding
import com.google.android.material.snackbar.Snackbar

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>,
            val hint: String)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> by lazy {
        mutableListOf(
            Question(text = resources.getStringArray(R.array.questions)[0],
                answers = listOf(resources.getStringArray(R.array.answers_to_question1)[0],
                                 resources.getStringArray(R.array.answers_to_question1)[1],
                                 resources.getStringArray(R.array.answers_to_question1)[2],
                                 resources.getStringArray(R.array.answers_to_question1)[3]),
                hint = resources.getStringArray(R.array.hints)[0]),
            Question(text = resources.getStringArray(R.array.questions)[1],
                answers = listOf(resources.getStringArray(R.array.answers_to_question2)[0],
                    resources.getStringArray(R.array.answers_to_question2)[1],
                    resources.getStringArray(R.array.answers_to_question2)[2],
                    resources.getStringArray(R.array.answers_to_question2)[3]),
                hint = resources.getStringArray(R.array.hints)[1]),
            Question(text = resources.getStringArray(R.array.questions)[2],
                answers = listOf(resources.getStringArray(R.array.answers_to_question3)[0],
                    resources.getStringArray(R.array.answers_to_question3)[1],
                    resources.getStringArray(R.array.answers_to_question3)[2],
                    resources.getStringArray(R.array.answers_to_question3)[3]),
                hint = resources.getStringArray(R.array.hints)[2]),
            Question(text = resources.getStringArray(R.array.questions)[3],
                answers = listOf(resources.getStringArray(R.array.answers_to_question4)[0],
                    resources.getStringArray(R.array.answers_to_question4)[1],
                    resources.getStringArray(R.array.answers_to_question4)[2],
                    resources.getStringArray(R.array.answers_to_question4)[3]),
                hint = resources.getStringArray(R.array.hints)[3]),
            Question(text = resources.getStringArray(R.array.questions)[4],
                answers = listOf(resources.getStringArray(R.array.answers_to_question5)[0],
                    resources.getStringArray(R.array.answers_to_question5)[1],
                    resources.getStringArray(R.array.answers_to_question5)[2],
                    resources.getStringArray(R.array.answers_to_question5)[3]),
                hint = resources.getStringArray(R.array.hints)[4]),
            Question(text = resources.getStringArray(R.array.questions)[5],
                answers = listOf(resources.getStringArray(R.array.answers_to_question6)[0],
                    resources.getStringArray(R.array.answers_to_question6)[1],
                    resources.getStringArray(R.array.answers_to_question6)[2],
                    resources.getStringArray(R.array.answers_to_question6)[3]),
                hint = resources.getStringArray(R.array.hints)[5]),
            Question(text = resources.getStringArray(R.array.questions)[6],
                answers = listOf(resources.getStringArray(R.array.answers_to_question7)[0],
                    resources.getStringArray(R.array.answers_to_question7)[1],
                    resources.getStringArray(R.array.answers_to_question7)[2],
                    resources.getStringArray(R.array.answers_to_question7)[3]),
                hint = resources.getStringArray(R.array.hints)[6]),
            Question(text = resources.getStringArray(R.array.questions)[7],
                answers = listOf(resources.getStringArray(R.array.answers_to_question8)[0],
                    resources.getStringArray(R.array.answers_to_question8)[1],
                    resources.getStringArray(R.array.answers_to_question8)[2],
                    resources.getStringArray(R.array.answers_to_question8)[3]),
                hint = resources.getStringArray(R.array.hints)[7]),
            Question(text = resources.getStringArray(R.array.questions)[8],
                answers = listOf(resources.getStringArray(R.array.answers_to_question9)[0],
                    resources.getStringArray(R.array.answers_to_question9)[1],
                    resources.getStringArray(R.array.answers_to_question9)[2],
                    resources.getStringArray(R.array.answers_to_question9)[3]),
                hint = resources.getStringArray(R.array.hints)[8]),
            Question(text = resources.getStringArray(R.array.questions)[9],
                answers = listOf(resources.getStringArray(R.array.answers_to_question10)[0],
                    resources.getStringArray(R.array.answers_to_question10)[1],
                    resources.getStringArray(R.array.answers_to_question10)[2],
                    resources.getStringArray(R.array.answers_to_question10)[3]),
                hint = resources.getStringArray(R.array.hints)[9])
    )
    }


    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var numQuestions = 0
    private var score = 0
    private var hintButtonPulsed = false
    private val scoreDefault = 10




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        val args = GameFragmentArgs.fromBundle(requireArguments())
        val levelTextView = binding.levelText
        when(args.selectedMode) {
            0 -> {
                numQuestions = 2
                levelTextView.text = getText(R.string.levelEasy)
            }
            1 -> {
                numQuestions = 4
                levelTextView.text= getText(R.string.levelMedium)
            }
            2 -> {
                numQuestions = 6
                levelTextView.text = getText(R.string.levelHard)
            }
        }



        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        if (hintButtonPulsed) {
                            score += (scoreDefault*questionIndex) / 2
                        }
                        else {
                            score += (scoreDefault * questionIndex)
                        }
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        hintButtonPulsed = false

                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        if (hintButtonPulsed) {
                            score += (scoreDefault*questionIndex) / 2
                        }
                        else {
                            score += (scoreDefault * questionIndex)
                        }
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(questionIndex,numQuestions, args.selectedMode, score))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(questionIndex,numQuestions, args.selectedMode, score))
                }
            }
        }

        binding.hintButton.setOnClickListener {
            hintButtonPulsed = true
            Snackbar.make(it, currentQuestion.hint, Snackbar.LENGTH_LONG)
                .show();
        }

        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions, score)
    }
}
