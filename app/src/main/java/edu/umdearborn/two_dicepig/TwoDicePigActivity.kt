package edu.umdearborn.two_dicepig

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import edu.umdearborn.two_dicepig.databinding.ActivityTwoDicePigBinding
import kotlin.random.Random

class TwoDicePigActivity : AppCompatActivity() {
    //vars
    private lateinit var binding : ActivityTwoDicePigBinding
    private var player1Total : Int = 0
    private var player2Total : Int = 0
    private var turnTotal : Int = 0
    private val winningScore : Int = 50
    private var die1 : Int = 0
    private var die2 : Int = 0
    private var currentPlayer : Int = Random.nextInt(1, 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        // initialization
        super.onCreate(savedInstanceState)
        binding = ActivityTwoDicePigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // make stuff invisible
        binding.playAgainBtn.visibility = View.INVISIBLE
        binding.victoryLabel.visibility = View.INVISIBLE

        // make totals zero
        binding.player1Total.text = "0"
        binding.player2Total.text = "0"

        // make turn total zero
        binding.turnTotal.text = "0"

        // update player
        if (currentPlayer == 1) {
            binding.currentPlayer.text = "P1"
        }

        else {
            binding.currentPlayer.text = "P2"
        }
    }
}