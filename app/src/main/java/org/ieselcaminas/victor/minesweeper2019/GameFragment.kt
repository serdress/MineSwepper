package org.ieselcaminas.victor.minesweeper2019


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        Toast.makeText(context, "Rows = $numRows Cols = $numCols",
            Toast.LENGTH_LONG).show()

        createButtons()

        return binding.root
    }


    private fun createButtons() {
        /*var boardTemp = Array<MineButton>(numRows)
        board = arrayOf(boardTemp) */

        board = Array(numRows) { row ->
                Array(numCols) { col ->
                MineButton(context!!, row,col)
            }
        }



    }


}
