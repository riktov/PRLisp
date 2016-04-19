(define balance 100)

(define (foo amount)
  (if (= balance amount)
    amount))

(define (withdraw amount)
  (if (>= balance amount)
      ;(begin (set! balance (- balance amount))
      ;       balance)
      amount))


(define new-withdraw
  (let ((balance 100))
    (lambda (amount)
      (if (>= balance amount)
          (begin (set! balance (- balance amount))
                 balance)
          "Insufficient funds"))))


(define (make-withdraw balance)
  (lambda (amount)
    (if (>= balance amount)
        (begin (set! balance (- balance amount))
               balance)
        "Insufficient funds")))
