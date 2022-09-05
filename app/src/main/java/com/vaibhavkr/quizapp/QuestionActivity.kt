package com.vaibhavkr.quizapp

import android.content.Context
import com.vaibhavkr.quizapp.Data.Question
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.vaibhavkr.quizapp.databinding.ActivityQuestionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuestionBinding
    var questions: List<Question>? = null
    var currrentques: Int = 0
    var selectedOption: Int = 0
    lateinit var markedOptions: ArrayList<IntArray>
    lateinit var time: CountDownTimer
    var timeLimit: Long = 1
    var green = Color.parseColor("#65e340")
    var normal = Color.parseColor("#000000")
    lateinit   var database: FirebaseDatabase
    lateinit   var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        database.reference.child("image").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val image = snapshot.getValue(String::class.java)
                Picasso.get().load(image).into(binding.profile)

            }

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.btnExit.setOnClickListener(){
            var intent = Intent(this, MainActivity::class.java)
            Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }

        binding.back.setOnClickListener(){
            var intent = Intent(this, MainActivity::class.java)
            Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }

        binding.opt1.setOnClickListener {
            var optn = click(markedOptions[currrentques][0])
            markedOptions[currrentques][0] = optn
            if(optn == 0) {
                binding.opt1tv.setTextColor(normal)
            }
            else {
                binding.opt1tv.setTextColor(green)
            }
        }

        binding.opt2.setOnClickListener {
            var optn = click(markedOptions[currrentques][1])
            markedOptions[currrentques][1] = optn
            if(optn == 0) {
                binding.opt2tv.setTextColor(normal)
            }
            else {
                binding.opt2tv.setTextColor(green)
            }
        }

        binding.opt3.setOnClickListener {
            var optn = click(markedOptions[currrentques][2])
            markedOptions[currrentques][2] = optn
            if(optn == 0) {
                binding.opt3tv.setTextColor(normal)
            }
            else {
                binding.opt3tv.setTextColor(green)
            }
        }

        binding.opt4.setOnClickListener {
            var optn = click(markedOptions[currrentques][3])
            markedOptions[currrentques][3] = optn
            if(optn == 0) {
                binding.opt4tv.setTextColor(normal)
            }
            else {
                binding.opt4tv.setTextColor(green)
            }
        }

        // Previous button and Next button
        binding.nextButton.setOnClickListener {
            nextQuestion()
        }
        binding.prevButton.setOnClickListener {
            prevQuestion()
        }

        //Question from API and timer
        lifecycleScope.launchWhenCreated {
var resultBody =RetrofitInstance.retrofit.getans().body()?.result
            questions = resultBody?.questions
            timeLimit = ((resultBody?.timeInMinutes!!.toLong())*(240*1000))
            selectedOption = questions!!.size
            withContext(Dispatchers.Main) {
                startQuestion()
            }
        }
    }

    private fun startQuestion() {
        markedOptions = ArrayList()
        for(selop in 0..selectedOption-1) {
            markedOptions.add(IntArray(4))
        }
        for(selop in 0..selectedOption-1) {
            for(jeop in 0..3) {
                markedOptions[selop][jeop] = 0
            }
        }

        time = object: CountDownTimer(timeLimit, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var secs = (millisUntilFinished/1000);
                var minsLeft = (secs/60)
                var secLeft = (secs%60)
                binding.timer.text = (minsLeft.toString()) + ": " + (secLeft.toString())
            }
            override fun onFinish() {
                submitques()
            }
        }

        /* start the timer */
        time.start()

        binding.ll.visibility = View.VISIBLE
        binding.prevButton.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE
      //  binding.quesCard.visibility = View.VISIBLE
        indexedView()
    }

    private fun nextQuestion() {
        if(currrentques == selectedOption-1){
            submitques()
            return
        }
        currrentques++
        indexedView()
    }

    private fun submitques() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("totalScoreEarned", calculateScore())
        startActivity(intent)
        time.cancel()
        finish()
    }

    private fun prevQuestion() {
        if(currrentques == 0) {
            Toast.makeText(this, "First com.vaibhavkr.quizapp.Data.Question!!", Toast.LENGTH_SHORT).show()
            return
        }
        currrentques--
        indexedView()
    }

    private fun indexedView() {
        if(currrentques == 0){
            binding.prevButton.visibility = View.GONE
        }
        else {
            binding.prevButton.visibility = View.VISIBLE
        }
        if(currrentques != selectedOption-1){
            binding.nextText.text = "NEXT"
        }
        else if(currrentques == selectedOption-1){
            binding.nextText.text = "SUBMIT"
        }

        binding.quesText.text = questions?.get(currrentques)?.lable
        binding.opt1tv.text = questions?.get(currrentques)?.options?.get(0)?.lable
        binding.opt2tv.text = questions?.get(currrentques)?.options?.get(1)?.lable
        binding.opt3tv.text = questions?.get(currrentques)?.options?.get(2)?.lable
        binding.opt4tv.text = questions?.get(currrentques)?.options?.get(3)?.lable

        /* Initialize colors on options */
        for(int in 0..3) {
            var color =  markedOptions[currrentques][int]
            if(color == 1) {
                if(int==0) {
                    binding.opt1tv.setTextColor(green)
                }
                else if(int==1) {
                    binding.opt2tv.setTextColor(green)
                }
                else if(int==2) {
                    binding.opt3tv.setTextColor(green)
                }
                else if(int==3) {
                    binding.opt4tv.setTextColor(green)
                }
            }
            else {
                if(int==0) {
                    binding.opt1tv.setTextColor(normal)
                }
                else if(int==1) {
                    binding.opt2tv.setTextColor(normal)
                }
                else if(int==2) {
                    binding.opt3tv.setTextColor(normal)
                }
                else if(int==3) {
                    binding.opt4tv.setTextColor(normal)
                }
            }
        }
    }
    private fun click(bool: Int): Int {
        var clicked = 0;
        if(bool == 0) clicked=1
        else clicked = 0
        return clicked
    }

    private fun calculateScore() : Int{
        var totalScore = 0
        for(int in 0..selectedOption-1) {

            var lst = questions?.get(int)!!.correct_answers
            var rightAnswers = lst.size
            var halfMarks = 0
            var numOfAnswersMarked = 0

            // Number of answers marked
            for(x in markedOptions[int]) {
                if(x == 1) {
                    numOfAnswersMarked++
                }
            }
            for(x in lst) {
                if(markedOptions[int][x-1] == 1) {
                    halfMarks = 1
                }
            }

            // check if user has marked any incorrect option so user will get 0, each question has 4 marks
            for(j in 0..3) {
                if(markedOptions[int][j] == 1) {
                    var cancel = 1
                    for(x in lst) {
                        if(x-1 == j) {
                            cancel = 0;
                            break
                        }
                    }
                    if(cancel == 1) {
                        halfMarks= 0
                    }
                }

            }
            if(halfMarks == 1) {
                if(numOfAnswersMarked == rightAnswers) totalScore += 4;
                else totalScore += 2
            }
        }
        return totalScore
    }

    override fun onBackPressed() {
        super.onBackPressed()
        time.cancel()
        finish()
    }
}