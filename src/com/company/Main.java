package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static String winner;
    public static StringBuilder result = new StringBuilder();
    public static ArrayList<String> cardsSam = new ArrayList<>();
    public static ArrayList<String> cardsDealer = new ArrayList<>();
    public static int resultSam=0;
    public static int resultDealer=0;

    public static void main(String[] args) throws FileNotFoundException {

        List<String> list = loadCards();
        ArrayDeque<String> cards = new ArrayDeque<>(list);
        System.out.println(cards);

        initialHand(cards);
        if(!blackJack(cardsSam,cardsDealer)){
            System.out.println(cards);
            drawCards(cards);
            determine(cardsSam, cardsDealer);
        }
        System.out.println(resultSam);
        System.out.println(resultDealer);
    }

    public static List<String> loadCards() throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        File file = new File("./cards.txt");
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            list.add(scanner.next());
        }
        Collections.shuffle(list);
        return list;
    }

    public static void drawCards(ArrayDeque<String> cards){
        String card1;
        String card2;
        while ((sum(cardsSam) == 0) && (sum(cardsDealer) == 0)){
            cardsSam.add(cards.pollLast());
            cardsDealer.add(cards.pollLast());
        }
        for(String s : cardsSam){
            resultSam += valueCheck(s);
        }
        for(String s : cardsDealer){
            resultDealer += valueCheck(s);
        }
        while ((sum(cardsSam) == 0) && (resultSam <= resultDealer) && (resultDealer < 21)){
            card1 = cards.pollLast();
            card2 = cards.pollLast();
            cardsSam.add(card1);
            resultSam += valueCheck(Objects.requireNonNull(card1));
            cardsDealer.add(card2);
            resultDealer += valueCheck(Objects.requireNonNull(card2));
        }
        while ((sum(cardsSam) == 1) && (resultSam >= resultDealer) && (resultDealer != 21) ){
            card1 = cards.pollLast();
            cardsDealer.add(card1);
            resultDealer += valueCheck(Objects.requireNonNull(card1));
        }
    }

    public static void determine (ArrayList<String> cardsSam, ArrayList<String> cardsDealer) {
       if(resultSam > 21){
            winner = "deal";
            formatResult(cardsSam, cardsDealer, winner, result);

        }
        else if((resultDealer > 21) || (resultSam > resultDealer)){
            winner = "sam";
            formatResult(cardsSam, cardsDealer, winner, result);
        }
        else if(resultSam < resultDealer){
            winner = "dealer";
            formatResult(cardsSam, cardsDealer, winner, result);
        }
        else {
            winner = "sam & dealer";
            formatResult(cardsSam, cardsDealer, winner, result);
        }

        System.out.println(result);
    }

    public static int valueCheck(String str){
        int value;
        String valueStr = str.substring(1);
        if(valueStr.equals("J") || valueStr.equals("Q") || valueStr.equals("K")){
            value = 10;
        }
        else if (valueStr.equals("A")){
            value = 11;
        }
        else {
            value =Integer.parseInt(valueStr);
        }
        return value;
    }

    //check player's score
    public static int sum (ArrayList<String> arrayList){
        int sum=0;
        for (String s : arrayList) {
            sum += valueCheck(s);
            if (sum >= 17 && sum <= 21) {
                return 1;
            }
            if (sum > 21) {
                return -1;
            }
        }
        return 0;
    }

    private static void formatResult(ArrayList<String> cardsSam, ArrayList<String> cardsDealer, String winner, StringBuilder result) {
        result.append(winner).append("\nsam: ");
        for(String s : cardsSam){
            result.append(s).append(", ");
        }
        result.delete(result.length()-2, result.length());
        result.append("\ndealer: ");
        for(String s : cardsDealer){
            result.append(s).append(", ");
        }
        result.delete(result.length()-2, result.length());
    }

    public static boolean blackJack(ArrayList<String> cardsSam, ArrayList<String> cardsDealer){
        for(String s : cardsSam){
            resultSam += valueCheck(s);
        }
        for(String s : cardsDealer){
            resultDealer += valueCheck(s);
        }

        if(resultSam == 21){
            winner = "sam";
            formatResult(cardsSam, cardsDealer, winner, result);
        }
        else if(resultDealer == 21){
            winner = "dealer";
            formatResult(cardsSam, cardsDealer, winner, result);
        }
        else if(resultSam==22 && resultDealer==22){
            winner = "dealer";
            formatResult(cardsSam, cardsDealer, winner, result);
        }
        else {
            resultSam = 0;
            resultDealer = 0;
            cardsSam.clear();
            cardsDealer.clear();
            return false;
        }
        System.out.println(result);
        return true;
    }

    public static void initialHand(ArrayDeque<String> cards){
        for (int i=0; i<2; i++){
            cardsSam.add(cards.pollLast());
            cardsDealer.add(cards.pollLast());
        }
    }
}
