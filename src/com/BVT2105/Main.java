package com.BVT2105;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("№1\t" + bell(3));
        System.out.println("№2\t" + translateWord("Shrimp") + "\n\t" + translateSentence("I like to eat honey waffles."));
        System.out.println("№3\t" + validColor("rgba(0,0,0,0.123456)"));
        System.out.println("№4\t" + stripUrlParams("https://edabit.com?a=1&b=2&a=2", "b"));
        System.out.println("№5\t" + getHashTags("Visualizing Science"));
        System.out.println("№6\t" + ulam(206));
        System.out.println("№7\t" + longestNonRepeatingSubstring("abcda"));
        System.out.println("№8\t" + convertToRoman(16));
        System.out.println("№9\t" + formula("16 * 10 = 160 = 40 + 120"));
        System.out.println("№10\t" + palindromeDescendant(11));
    }


    // №1 число Белла
    // в основной функции мы по входному числу проходим цикл, словно по массиву и запускаем основную функцию
    public static int bell(int number) {
        int result = 0;
        for (int k = 1; k <= number; k++) {
            result += getStarling(number, k);
        }
        return result;
    }
    // основная функция проверяющая ращбивается ли входные данные на непустые подмножества
    private static int getStarling(int n, int k) {
        if (n == k) return 1;
        if ((n > 0 && k == 0) || n < k) return 0;
        return getStarling(n - 1, k - 1) + k * getStarling(n - 1, k);
    }

    // №2 Поросячья латынь
    //перевод слова на поросячью латынь
    public static String translateWord(String word) {
        // Если слова нет, возвращает пустую строку
        if (word.isEmpty()) return "";
        // если начинается с гласной, добавляет yay
        if (word.matches("^(?i:[aeyuio]).*")) return word + "yay";
            // иначе слово начинается с согласного, тогда перемещаем первую букву в конец и добавляем ay
        else {
            int ch = word.indexOf(word.replaceAll("[^aeyuio]", "").charAt(0));
            if (word.charAt(0) > 96) return word.substring(ch) + word.substring(0, ch) + "ay";
            else
                return (char) ((int) word.charAt(ch) - 32) + word.substring(ch + 1) + word.substring(0, ch).toLowerCase() + "ay";
        }
    }
    // перевод предложения на поросячью латынь
    public static String translateSentence(String sentence) {
        // с помощью стрингбилдера заносим слова в предложение
        StringBuilder result = new StringBuilder();
        int start = 0;
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.substring(i, i + 1).matches("\\W")) {
                result.append(translateWord(sentence.substring(start, i)));
                result.append(sentence.charAt(i));
                start = i + 1;
            }
        }
        return result.toString();
    }


    // №3 RGB формат
    // учитывая параметры RGB(A) CSS, определяет является ли формат допустимым
    public static boolean validColor(String rgb) {
        // создадим массив строк и разобьем принимаемую через запятую
        String[] strings = rgb.split("[(,)]");

        try {
            return (String.format("rgb(%s,%s,%s)", strings[1], strings[2], strings[3]).equals(rgb) && (Integer.parseInt(strings[1]) >= 0 && Integer.parseInt(strings[1]) <= 255
                    && Integer.parseInt(strings[2]) >= 0 && Integer.parseInt(strings[2]) <= 255 && Integer.parseInt(strings[3]) >= 0 && Integer.parseInt(strings[3]) <= 255))
                    || (String.format("rgba(%s,%s,%s,%s)", strings[1], strings[2], strings[3], strings[4]).equals(rgb) && (Integer.parseInt(strings[1]) >= 0 && Integer.parseInt(strings[1]) <= 255
                    && Integer.parseInt(strings[2]) >= 0 && Integer.parseInt(strings[2]) <= 255 && Integer.parseInt(strings[3]) >= 0 && Integer.parseInt(strings[3]) <= 255)
                    && Float.parseFloat(strings[4]) >= 0 && Float.parseFloat(strings[4]) <= 1);
        } catch (Exception e) {
            return false;
        }
    }

    // №4 URL без дублёров
    // принимает URL строку, удаляет дублирующиеся параметры запроса и параметры второго аргумента
    public static String stripUrlParams(String url, String... value) {
        // если не содержится знака вопроса, то значит нет дублирующихся параметров
        if (!url.contains("?")) return url;
        // разделим массив строк по знаку вопроса
        String[] parts = url.split("\\?");
        StringBuilder sb = new StringBuilder(parts[0]).append("?");
        // обхединим после знака амперсанда
        String[] params = parts[1].split("&");
        HashMap<String, String> map = new HashMap<>();
        // для строки параметров пройдясь по параметрам объединим все где встречается знак =
        for (String param : params) {
            String[] str = param.split("=");
            map.put(str[0], str[1]);
        }

        LinkedHashSet<String> strip = new LinkedHashSet<>(Arrays.asList(value));

        int count = 1;
        // пройдемся по счетчику по ключу, добавим амперсанд, добавим ключ, увеличим счетчик
        for (String key : map.keySet()) {
            if (!strip.contains(key)) {
                if (count > 1)
                    sb.append("&");
                sb.append(key).append("=").append(map.get(key));
                count++;
            }
        }

        return sb.toString();
    }

    // №5 Хештеги
    // функция, извлекающая три самых длинных слова из заголовка газеты и преобразует их в хэштеги
    public static ArrayList getHashTags(String string) {
        // создадим из входной строки список, в котором уберем все ненужные символы и добавим пробел
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(string.replaceAll("[,.?!;:\"\']", "").split(" ")));
        ArrayList<String> result = new ArrayList<>();
        // если в списке меньше трех слов, то расположим слова по длине в порядке убывания
        if (temp.size() < 3) {
            temp.sort((String o1, String o2) -> Integer.compare(o2.length(), o1.length()));
            for (String str : temp) result.add("#" + str.toLowerCase());
            return result;
        }
        // пока наш счетчик меньше трех нужных хештегов, выводим их, заменяя первую букву на строчную и добавляя #
        int count = 0;
        while (count < 3) {
            String max = "";
            for (String str : temp) {
                if (str.length() > max.length()) {
                    max = str;
                }
            }
            temp.remove(max);
            result.add("#" + max.toLowerCase());
            count++;
        }
        return result;
    }

    // №6 последовательность Улама
    public static int ulam(int num) {
        ArrayList<Integer> arr = new ArrayList<>();
        // добавим в последовательность первые два числа
        arr.add(1);
        arr.add(2);
        int i, j;
        // Напишем вложенный цикл для всех остальных чисел в последовательности
        // добавляем число в последовательность только если его можно сделать единственным образом из двух других различных чисел которые уже есть в последовательности
        for (i = 3, j = 2; j < num; i++) {
            int count = 0;
            for (int k = 0; k < arr.size() - 1; k++) {
                for (int l = k + 1; l < arr.size(); l++) {
                    // если можем собрать число, то увеличиваем счетчик
                    if (arr.get(k) + arr.get(l) == i)
                        count++;
                    // если собрали два числа, выходим из цикла
                    if (count > 1)
                        break;
                }
                if (count > 1)
                    break;
            }
            // если смогли собрать число, добавляем его в список и на следующую итерацию цикла
            if (count == 1) {
                arr.add(i);
                j++;
            }
        }
        return i - 1;
    }

    // №7 Самая длинная неповторяющаяся подстрока
    public static String longestNonRepeatingSubstring(String str) {
        // введем параметры для работы
        int i;
        int temp;
        int maximumLen = 0;
        int start = 0;
        int st = 0;
        // создадим пары символа и числа
        HashMap<Character, Integer> pos = new HashMap<>();
        pos.put(str.charAt(0), 0);
        // пройдемся циклом по строке
        for (i = 1; i < str.length(); i++)
        {
            // Если в хешмапе нет символа, то вносим его
            if (!pos.containsKey(str.charAt(i))) pos.put(str.charAt(i), i);
                // Если символ есть, то проверяем больше ли он st, если да, то запоминаем в темп
            else
            {
                if (pos.get(str.charAt(i)) >= st)
                {
                    temp = i - st;
                    // перезапоминаем максимум
                    if (maximumLen < temp)
                    {
                        maximumLen = temp;
                        start = st;
                    }
                    st = pos.get(str.charAt(i)) + 1;
                }
                // Если нашли подстроку больше, заменяем старую на нее
                pos.replace(str.charAt(i), i);
            }
        }
        if (maximumLen < i - st)
        {
            maximumLen = i - st;
            start = st;
        }
        // собираем конечную строку
        return str.substring(start, start + maximumLen);
    }

    // №8 Римские число
    public static String convertToRoman(int number) {
        // создадим стрингбилдер, с которым будем работать
        StringBuilder result = new StringBuilder();
        // пока наше число не нулевое, последовательно с большего до меньшего разряда преобразуем в римское
        while (number != 0) {
            if (number >= 1000) {
                number -= 1000;
                result.append("M");
            }
            else if(number >= 900){
                number -= 900;
                result.append("CM");
            }
            else if(number >= 500){
                number -= 500;
                result.append("D");
            }
            else if(number >= 400){
                number -= 400;
                result.append("CD");
            }
            else if(number >= 100){
                number -= 100;
                result.append("C");
            }
            else if(number >= 90){
                number -= 90;
                result.append("XC");
            }
            else if(number >= 50){
                number -= 50;
                result.append("L");
            }
            else if(number >= 40){
                number -= 40;
                result.append("XL");
            }
            else if(number >= 10){
                number -= 10;
                result.append("X");
            }
            else if(number >= 9){
                number -= 9;
                result.append("IX");
            }
            else if(number >= 5){
                number -= 5;
                result.append("V");
            }
            else if(number >= 4){
                number -= 4;
                result.append("IV");
            }
            else if(number > 0){
                number -= 1;
                result.append("I");
            }
        }
        return result.toString();
    }


    // №9 функция, возвращающая правильность вводимой формулы
    public static boolean formula(String str) {
        // создадим массив равенств
        String[] equations = str.split("=");
        for (int i = 0; i < equations.length-1; i++)
        {
            // с помощью саб-функции калькулятора проверим верность равенства
            if(calculator(equations[i]) != calculator(equations[i+1])) return false;
        }
        return true;

    }
    // напишем функцию калькулирования для форматирования данных
    public static int calculator(String string) {
        string = string.replaceAll("[()]", "").replaceAll(" ", "");
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            String[] operation;
            operation = string.split("\\*", 2);
            if (operation.length > 1) return calculator(operation[0]) * calculator(operation[1]);

            operation = string.split("/", 2);
            if (operation.length > 1) return calculator(operation[0]) / calculator(operation[1]);

            operation = string.split("\\+", 2);
            if (operation.length > 1) return calculator(operation[0]) + calculator(operation[1]);

            operation = string.split("-", 2);
            if (operation.length > 1) return calculator(operation[0]) - calculator(operation[1]);

            return 0;
        }
    }


    // №10 Функция, которая определеяет является ли само число палиндромом или любой из его наследников вплоть до 2-цифр
    public static boolean palindromeDescendant(int number) {
        boolean isSym = false;
        while(number > 9) {
            // увеличиваем число на его сумму проверяя симметричность, если число окажется симметричным, то выйдем из цикла и вернем true,
            // если за весь проход симметричности не будет, то флаг останется false и вернется false
            if(isSymmetrical(number)) {
                isSym = true;
                break;
            }
            number = getSumOfDigits(number);
        }
        return isSym;
    }
    // функция определения симметричности
    public static boolean isSymmetrical(int num) {
        int reversenum = 0, n = num;
        if(n < 0) n = n * -1;
        while(n != 0) {
            reversenum = reversenum * 10;
            reversenum = reversenum + n % 10;
            n = n / 10;
        }
        return(reversenum == num);
    }
    // функция для суммирования цифр
    public static int getSumOfDigits(int n) {
        String iString = Integer.toString(n);
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < iString.length() - 1; i += 2) {
            int num1 = Integer.parseInt(Character.toString(iString.charAt(i)));
            int num2 = Integer.parseInt(Character.toString(iString.charAt(i+1)));
            int num = num1 + num2;
            sb.append(num);
        }

        return Integer.parseInt(sb.toString());
    }

}
