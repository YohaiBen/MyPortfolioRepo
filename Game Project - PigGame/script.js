'use strict';

// selectting elements
const player0El = document.querySelector('.player--0');
const player1El = document.querySelector('.player--1');
let score0EL = document.getElementById('score--0');
let score1EL = document.getElementById('score--1');
const diceEl = document.querySelector('.dice');
const btnNew = document.querySelector('.btn--new');
const btnRoll = document.querySelector('.btn--roll');
const btnHold = document.querySelector('.btn--hold');
const currentOEl = document.getElementById('current--0');
const current1El = document.getElementById('current--1');

//start Conditions
let playing, scores, activePlayer, currentScore;

function initGame() {
  playing = true;
  scores = [0, 0];
  currentScore = 0;
  activePlayer = 0;

  currentOEl.textContent = 0;
  current1El.textContent = 0;
  score0EL.textContent = 0;
  score1EL.textContent = 0;

  diceEl.classList.add('hidden');
  player0El.classList.remove('player--winner');
  player1El.classList.remove('player--winner');
  player0El.classList.add('player--active');
  player1El.classList.remove('player--active');
}
initGame();
//roll the dice func
function rollDice() {
  if (playing) {
    //random dice roll
    const dice = Math.trunc(Math.random() * 6) + 1;
    console.log(dice);
    diceEl.classList.remove('hidden');
    diceEl.src = `dice-${dice}.png`;
    // display the resault to the client
    if (dice !== 1) {
      currentScore += dice;
      document.getElementById(
        `current--${activePlayer}`
      ).textContent = currentScore;
    } else {
      reasetScore();
      changePlayer();
    }
  }
}
function reasetScore() {
  scores[activePlayer] = 0;
  if (activePlayer === 0) {
    score0EL.textContent = 0;
  } else {
    score1EL.textContent = 0;
  }
}
function changePlayer() {
  document.getElementById(`current--${activePlayer}`).textContent = 0;
  activePlayer = activePlayer === 0 ? 1 : 0;
  player0El.classList.toggle('player--active');
  player1El.classList.toggle('player--active');
  currentScore = 0;
}
function hold() {
  if (playing) {
    scores[activePlayer] += currentScore;
    document.getElementById(`score--${activePlayer}`).textContent =
      scores[activePlayer];
    if (scores[activePlayer] >= 20) {
      playing = false;

      document
        .querySelector(`.player--${activePlayer}`)
        .classList.add('player--winner');
      document
        .querySelector(`.player--${activePlayer}`)
        .classList.remove('player--active');
      diceEl.classList.remove('hidden');
    } else {
      changePlayer();
    }
  }
}

//Rolling dice functionality
btnRoll.addEventListener('click', rollDice);
btnHold.addEventListener('click', hold);
btnNew.addEventListener('click', initGame);
