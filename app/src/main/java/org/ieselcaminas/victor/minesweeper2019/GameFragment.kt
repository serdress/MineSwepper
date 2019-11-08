package org.ieselcaminas.victor.minesweeper2019


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
            false)

        binding.buttonWin.setOnClickListener() {
            it.findNavController().navigate(GameFragmentDirections.actionGameFragmentToWonFragment())
        }

        binding.buttonLose.setOnClickListener() {
            it.findNavController().navigate(GameFragmentDirections.actionGameFragmentToLoseFragment())
        }

        var args = GameFragmentArgs.fromBundle(arguments!!)
        numRows = args.numRows
        numCols = args.numCols
        bombMatrix = BombMatrix(numRows, numCols, (numRows * numCols) / 6)
        Toast.makeText(context, "Rows = $numRows Cols = $numCols",
            Toast.LENGTH_LONG).show()

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
        for (col in 0..numCols-1) {
            for (row in 0..numRows-1) {
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
        /* for (line in board) {
            for (button in line) {
                binding.gridLayout.addView(button)
                button.setOnClickListener() {
                    println("row: ${button.row} col: ${button.col}")
                }
            }
        } */


    }

    private fun open(row: Int, col: Int) {
        if (bombMatrix.board[row][col] == 1) {
            board[row][col].setImageResource(R.drawable.num1)
            board[row][col].state = StateType.OPEN
           // board[row][col].visibility = View.INVISIBLE
            return
        }
        if (bombMatrix.board[row][col] == 2) {
            board[row][col].setImageResource(R.drawable.num2)
            board[row][col].state = StateType.OPEN
            // board[row][col].visibility = View.INVISIBLE
            return
        }
        if (bombMatrix.board[row][col] == 3) {
            board[row][col].setImageResource(R.drawable.num3)
            board[row][col].state = StateType.OPEN
            // board[row][col].visibility = View.INVISIBLE
            return
        }
        if (bombMatrix.board[row][col] == -1) {
            board[row][col].setImageResource(R.drawable.bomb)
            board[row][col].state = StateType.OPEN
            return
        }
        if (!bombMatrix.isValid(row, col)) {
            return
        }
        board[row][col].state = StateType.OPEN
        board[row][col].visibility = View.INVISIBLE
        if (bombMatrix.value(row, col) != 0) { return }

        for (i in row -1 .. row + 1) {
            for (j in col -1 .. col +1) {
                if (bombMatrix.isValid(i,j)) {
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
