package org.ieselcaminas.victor.minesweeper2019


import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.AdaptiveIconDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import org.ieselcaminas.victor.minesweeper2019.databinding.FragmentGameBinding
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity


/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    lateinit var binding: FragmentGameBinding
    lateinit var board: Array<Array<MineButton>>
    lateinit var bombMatrix: BombMatrix
    var numRows: Int = 0
    var numCols: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game, container,
            false
        )

       /* binding.buttonWin.setOnClickListener() {
            it.findNavController()
                .navigate(GameFragmentDirections.actionGameFragmentToWonFragment())
        }

        binding.buttonLose.setOnClickListener() {
            it.findNavController()
                .navigate(GameFragmentDirections.actionGameFragmentToLoseFragment())
        }*/

        var args = GameFragmentArgs.fromBundle(arguments!!)
        numRows = args.numRows
        numCols = args.numCols
        bombMatrix = BombMatrix(numRows, numCols, (numRows * numCols) / 6)
        Toast.makeText(
            context, "Rows = $numRows Cols = $numCols",
            Toast.LENGTH_LONG
        ).show()

        binding.textViewBombs.text = ((numRows * numCols) / 6).toString()
        createButtons()

        return binding.root
    }


    private fun createButtons() {
        board = Array(numRows) { row ->
            Array(numCols) { col ->
                MineButton(context!!, row, col)
            }
        }
        binding.gridLayout.columnCount = numCols
        binding.gridLayout.rowCount = numRows
        for (col in 0..numCols - 1) {
            for (row in 0..numRows - 1) {
                var frameLayout = FrameLayout(context!!)
                val backgroundImageView = ImageView(context!!)
                backgroundImageView.setImageResource(R.drawable.back)
                setSizeBackView(backgroundImageView)
                frameLayout.addView(backgroundImageView)


                frameLayout.addView(board[row][col])
                board[row][col].alpha = 0.7f
                binding.gridLayout.addView(frameLayout)

                board[row][col].setOnClickListener() {
                    val button = it as MineButton
                    if (button.state == StateType.CLOSED) {
                        open(button.row, button.col)
                        println("row: ${button.row} col: ${button.col}")
                        // button.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun showBombs() {
        for (row in 0..numCols - 1) {
            for (col in 0..numRows - 1) {
                if (bombMatrix.board[row][col] == -1) board[row][col].setImageResource(R.drawable.bomb)

            }
        }
    }

    private fun gameOver() {
        for (col in 0..numCols - 1) {
            for (row in 0..numRows - 1) {
                board[row][col].setOnClickListener(null)
                board[row][col].setOnLongClickListener(null)
                board[row][col].setOnTouchListener(null)
            }
        }
        basicAlert(this.view!!)
    }


    fun basicAlert(view: View){

        val builder = AlertDialog.Builder(context)

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            restartGame()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(context,
                "No", Toast.LENGTH_SHORT).show()
        }
        val neutralButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(context,
                "Maybe", Toast.LENGTH_SHORT).show()
        }
        with(builder)
        {
            setTitle("You loose")
            setMessage("Do you want to restart game?")
            setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("No", negativeButtonClick)
            setNeutralButton("Maybe", neutralButtonClick)
            show()
        }
    }

    private fun restartGame() {
        val i = Intent(context, MainActivity::class.java) //change it to your main class
        //the following 2 tags are for clearing the backStack and start fresh
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    private fun tryOpenNumber(row: Int, col: Int, number: Int, drawable: Int): Boolean {
        if (bombMatrix.board[row][col] == number) {
            board[row][col].setImageResource(drawable)
            board[row][col].state = StateType.OPEN
            return true
        }
        return false
    }

    private fun open(row: Int, col: Int) {



        if (bombMatrix.board[row][col] == -1) {
            showBombs()
            gameOver()
            return
        }

        if (tryOpenNumber(row, col, 1, R.drawable.num1)) return
        if (tryOpenNumber(row, col, 2, R.drawable.num2)) return
        if (tryOpenNumber(row, col, 3, R.drawable.num3)) return
        if (tryOpenNumber(row, col, 4, R.drawable.num4)) return
        if (tryOpenNumber(row, col, 5, R.drawable.num5)) return
        if (tryOpenNumber(row, col, 6, R.drawable.num6)) return
        if (tryOpenNumber(row, col, 7, R.drawable.num7)) return
        if (tryOpenNumber(row, col, 8, R.drawable.num8)) return

        board[row][col].state = StateType.OPEN
        board[row][col].visibility = View.INVISIBLE

        if (bombMatrix.value(row, col) != 0) {
            return
        }

        for (i in row - 1..row + 1) {
            for (j in col - 1..col + 1) {
                if (bombMatrix.isValid(i, j)) {
                    if (!(i == row && j == col) && board[i][j].state == StateType.CLOSED) {
                        open(i, j)
                    }
                }
            }
        }
    }

    private fun setSizeBackView(backgroundImageView: ImageView) {
        var layoutParams = LinearLayout.LayoutParams(SIZE, SIZE)
        backgroundImageView.setPadding(0, 0, 0, 0)
        backgroundImageView.scaleType = ImageView.ScaleType.CENTER
        backgroundImageView.adjustViewBounds = true
        backgroundImageView.layoutParams = layoutParams
    }


}
