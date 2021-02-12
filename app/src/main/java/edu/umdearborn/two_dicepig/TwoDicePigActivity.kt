package edu.umdearborn.two_dicepig

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.transition.Hold
import edu.umdearborn.two_dicepig.databinding.ActivityTwoDicePigBinding
import kotlin.random.Random

class TwoDicePigActivity : AppCompatActivity() {
    //vars
    private lateinit var binding : ActivityTwoDicePigBinding
    private var playerTotals : MutableList<Int> = mutableListOf(0, 0)
    private lateinit var playerTotalLabels : List<TextView>
    private var turnTotal : Int = 0
    private val winningScore : Int = 50
    private var die = arrayOf<Int>(0, 0)
    private lateinit var dieImages : MutableList<ImageView>
    private var currentPlayer : Int = Random.nextInt(0, 2)
    private var diceVisible : Boolean = false
    private var endVisible : Boolean = false

    //for errors
    val TAG = "TWO-DICE-PIG"

    override fun onCreate(savedInstanceState: Bundle?) {
        // initialization
        super.onCreate(savedInstanceState)
        binding = ActivityTwoDicePigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // make stuff invisible
        ToggleVisibilityOfEnd(true)

        // make totals zero
        playerTotalLabels = mutableListOf(binding.player1Total, binding.player2Total)
        playerTotalLabels[0].text = "0"
        playerTotalLabels[1].text = "0"

        // make turn total zero
        binding.turnTotal.text = "0"

        // update current player label
        UpdateCurrentPlayerLabel()

        // make die invisible
        ToggleVisibilityOfDie(true)

        // array of die
        dieImages = mutableListOf(binding.dice1, binding.dice2)


        // set up roll dice button
        RollDiceBtnInit()

        // set up hold button
        HoldBtnInit()
    }

    // update die images
    private fun UpdateDieImages() {
        for (i in 0..1) {
            when (die[i]) {
                1 -> dieImages[i].setImageResource(R.drawable.dice1)
                2 -> dieImages[i].setImageResource(R.drawable.dice2)
                3 -> dieImages[i].setImageResource(R.drawable.dice3)
                4 -> dieImages[i].setImageResource(R.drawable.dice4)
                5 -> dieImages[i].setImageResource(R.drawable.dice5)
                6 -> dieImages[i].setImageResource(R.drawable.dice6)
                else -> {
                    Log.e(TAG ,"Die $i not in range 1-6")
                }
            }
        }
    }

    // set handler for clicking roll dice button
    private fun RollDiceBtnInit() {
        // when click roll dice button
        binding.rollDiceBtn.setOnClickListener {
            // roll the dice
            die[0] = Random.nextInt(1, 7)
            die[1] = Random.nextInt(1, 7)

            // update die images
            UpdateDieImages()

            // if dice are invisible, make them visible
            if (!diceVisible) {
                ToggleVisibilityOfDie(false)
            }

            // enable hold button if it's not
            if (!binding.holdBtn.isEnabled) {
                binding.holdBtn.isEnabled = true
            }

            // rolled two ones
            if (die[0] == 1 && die[1] == 1) {
                // current player loses all of their points
                playerTotals[currentPlayer] = 0

                // make turn total zero
                turnTotal = 0

                // update turn total in GUI
                binding.turnTotal.text = turnTotal.toString()

                // other person's turn
                currentPlayer = (currentPlayer + 1) % 2

                // update label
                UpdateCurrentPlayerLabel()

                // make dice invisible
                ToggleVisibilityOfDie(true)
            }

            // rolled one one
            else if (die[0] == 1 || die[1] == 1) {
                // make turn total zero
                turnTotal = 0

                // update turn total in GUI
                binding.turnTotal.text = turnTotal.toString()

                // other person's turn
                currentPlayer = (currentPlayer + 1) % 2

                // update label
                UpdateCurrentPlayerLabel()

                // make dice invisible
                ToggleVisibilityOfDie(true)
            }

            // otherwise
            else {
                // dice 1 = dice 2 but not both 1
                if (die[0] == die[1]) {
                    binding.holdBtn.isEnabled = false
                }

                // add values of die to turn total
                turnTotal += die[0]
                turnTotal += die[1]

                // update turn total in GUI
                binding.turnTotal.text = turnTotal.toString()
            }
        }
    }

    // set handler for when hold button is clicked
    private fun HoldBtnInit() {
        binding.holdBtn.setOnClickListener {
            // add to player's total
            playerTotals[currentPlayer] += turnTotal

            // update player's total in GUI
            playerTotalLabels[currentPlayer].text = playerTotals[currentPlayer].toString()

            // make dice invisible
            ToggleVisibilityOfDie(true)

            // make turn total zero
            turnTotal = 0

            // update turn total in GUI
            binding.turnTotal.text = turnTotal.toString()

            // current player won
            if (isWinner()) {
                // disable buttons
                binding.holdBtn.isEnabled = false
                binding.rollDiceBtn.isEnabled = false

                // make victory message and play again button visible
                ToggleVisibilityOfEnd(false)

                // make dice images gone
                dieImages[0].visibility = View.GONE
                dieImages[1].visibility = View.GONE

                //set victory message
                if (currentPlayer == 0) {
                    binding.victoryLabel.setText(R.string.player_one_victory_msg)
                }

                else {
                    binding.victoryLabel.setText(R.string.player_two_victory_msg)
                }
            }

            // other player's turn
            else {
                currentPlayer = (currentPlayer + 1) % 2
                UpdateCurrentPlayerLabel()
            }
        }
    }

    // update current player label
    private fun UpdateCurrentPlayerLabel() {
        if (currentPlayer == 0) {
            binding.currentPlayer.text = "P1"
        } else {
            binding.currentPlayer.text = "P2"
        }
    }

    // toggle visibility of die
    // true = invisible    false = visible
    private fun ToggleVisibilityOfDie(toggle : Boolean) {
        // make invisible
        if (toggle) {
            binding.dice1.visibility = View.INVISIBLE
            binding.dice2.visibility = View.INVISIBLE
            diceVisible = false
        }

        // make visible
        else {
            binding.dice1.visibility = View.VISIBLE
            binding.dice2.visibility = View.VISIBLE
            diceVisible = true
        }
    }

    // toggle visibility of victory message and play again button
    // true = invisible    false = visible
    private fun ToggleVisibilityOfEnd(toggle : Boolean) {
        if (toggle) {
            binding.playAgainBtn.visibility = View.INVISIBLE
            binding.victoryLabel.visibility = View.INVISIBLE
            endVisible = false
        }

        else {
            binding.playAgainBtn.visibility = View.VISIBLE
            binding.victoryLabel.visibility = View.VISIBLE
            endVisible = true
        }
    }

    // determine if there is a winner
    private fun isWinner() : Boolean {
        return playerTotals[currentPlayer] >= winningScore
    }
}