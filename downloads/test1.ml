module type FichierLecture2Car =
sig
  type zero
  type _ suc
  type _ fichier
  val ouvrir : string -> zero fichier
  val lire : 'a fichier -> char * ('a suc) fichier
  val fermer : ((zero suc) suc) fichier -> unit
end

module type FichierLecturePairCar =
sig
  type pair
  type impair
  type _ fichier
  val ouvrir : string -> (pair * impair) fichier
  val lire : ('a * 'b) fichier -> (char * ('b * 'a) fichier)
  val fermer : (pair * impair) fichier -> unit
end

module type PerfectTree =
sig
  type zero
  type _ suc
  type ('a, _) perfect_tree =
    | Empty : ('a, zero) perfect_tree
    | Node : 'a * ('a * 'a, 'p) perfect_tree -> ('a, 'p suc) perfect_tree

  val split : type a d. (a * a, d) perfect_tree -> (a, d) perfect_tree * (a, d) perfect_tree
  val merge : type a d. (a, d) perfect_tree * (a, d) perfect_tree -> (a * a, d) perfect_tree
end;;

let test_tree : (int * int, (zero suc) suc) perfect_tree = Node ((1, 1), Node (((2, 2), (3, 3)), Empty));;

module PerfectTreeImpl : PerfectTree =
struct
  let rec split t =
    match t with
    | Empty -> Empty, Empty
    | Node ((h1, h2), q) -> let q1, q2 = split q
      in Node (h1, q1), Node (h2, q2)

  let rec merge (t1, t2) =
    match t1, t2 with
    | Empty, Empty -> Empty
    | Node (h1, q1), Node (h2, q2) -> Node ((h1, h2), merge (q1, q2))
end
