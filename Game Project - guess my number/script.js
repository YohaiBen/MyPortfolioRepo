'use strict';

class NumberGuess {
  score;
  highscore;
  answer;

  getScore() {
    return this.score;
  }
  getHighScorh() {
    return this.highscore;
  }
  getAnswer() {
    return this.answer;
  }

  constructor(score, highscore) {
    this.score = score;
    this.highscore = highscore;
  }
}
function getRandomNum1_20() {
  return Math.trunc(Math.random() * 20) + 1;
}
function displayMessage(message) {
  document.querySelector('.message').textContent = message;
}
let secretNumber = getRandomNum1_20();
document.querySelector('.number').textContent = '?'; //secretNumber;
let score = 20;
let highscore = 0;

document.querySelector('.check').addEventListener('click', guessFunc);
function guessFunc() {
  const guess = Number(document.querySelector('.guess').value);
  console.log(guess, typeof guess);
  if (score > 0) {
    if (!guess) {
      document.querySelector('.message').textContent = 'No Number ❌';
    } else if (guess === secretNumber) {
      displayMessage('Correct Number! 🥇');
      document.querySelector('body').style.backgroundColor = '#60b347';
      document.querySelector('.number').style.width = '30rem';
      document.querySelector('.number').textContent = secretNumber;
      if (highscore < score) {
        highscore = score;
        document.querySelector('.highscore').textContent = highscore;
      }
    } else if (guess > secretNumber) {
      displayMessage('Too High!📈');
      score--;
      document.querySelector('.score').textContent = score;
    } else if (guess < secretNumber) {
      displayMessage('Too Low!📉');
      score--;
      document.querySelector('.score').textContent = score;
    }
  } else {
    displayMessage('you Lose the game ❌');
  }
}
document.querySelector('.again').addEventListener('click', again);
function again() {
  document.querySelector('body').style.backgroundColor = ' #222';
  displayMessage('Start guessing...⁉');
  secretNumber = getRandomNum1_20();
  document.querySelector('.number').textContent = '?';
  score = 20;
  document.querySelector('.score').textContent = score;
  document.querySelector('.guess').value = '';
}

// }
