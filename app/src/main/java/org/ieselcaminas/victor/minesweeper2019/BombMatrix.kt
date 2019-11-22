package org.ieselcaminas.victor.minesweeper2019

import android.opengl.ETC1.isValid
import kotlin.random.Random

class BombMatrix(val numRows: Int, val numCols: Int, numBombs: Int) {

    val BOMB_INT = -1
    lateinit var board: Array<Array<Int>>

    init {
        board = Array(numRows) { row ->
            Array(numCols) { col ->
                0
            }
        }
        createBombs(numBombs)
        calculateNumbers()
        printBoard()
    }

    private fun calculateNumbers() {
        for (row in 0..numRows - 1) {
            for (col in 0..numCols - 1) {
                if (board[row][col] != BOMB_INT) {
                    calculateNumbers(row, col)
                }
            }
        }
    }

    private fun calculateNumbers(row: Int, col: Int) {
        var counter = 0
        for (i in col - 1..col + 1) {
            for (j in row - 1..row + 1) {
                if (!(i == col && j == row)) {
                    if (isValid(j, i)) {
                        if (board[j][i] == BOMB_INT) {
                            counter++
                        }

                    }
                }
            }
        }
        board[row][col] = counter
    }

    fun isValid(row: Int, col: Int): Boolean {
        if (row < 0 || row >= numRows) {
            return false
        }
        if (col < 0 || col >= numCols) {
            return false
        }
        return true
    }

    private fun createBombs(numBombs: Int) {
        var counter = 0
        while (counter < numBombs) {
            val rowAlea = Random.nextInt(0, numRows)
            val colAlea = Random.nextInt(0, numCols)
            if (board[rowAlea][colAlea] != BOMB_INT) {
                board[rowAlea][colAlea] = BOMB_INT
                counter++

            }
        }
    }

    private fun printBoard() {
        for (row in 0..board.size - 1) {
            for (col in 0..board[0].size - 1) {
                print(" ${board[row][col]} ")
            }
            println()
        }
    }


    public fun value(row: Int, col: Int): Int {
        return board[row][col]
    }

}