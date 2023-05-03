using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace NGram
{
    class Program
    {
        static void Main(string[] args)
        {
            bool NumGood = false;

            string userNumber;
            int userNumInt = 0;

            while(NumGood != true)
            {
                try
                {
                    Console.Write("\nEnter Number: ");
                    userNumber = Console.ReadLine();
                    userNumInt = Convert.ToInt32(userNumber);
                    if (userNumInt != 0)
                        NumGood = true;
                }
                catch (Exception)
                {
                    Console.WriteLine("DANGER INCORRECT NUMBER ADDED");
                    Console.WriteLine("Enter a numeric whole number.");
                }
            }

            bool TxtGood = false;
            string fileName = "";
            List<string> input = new List<string>();

            while (TxtGood != true)
            {
                try
                {
                    Console.Write("\nEnter File: ");
                    fileName = Console.ReadLine();
                    input = StripInput(fileName);
                    if (input != null)
                        TxtGood = true;
                }
                catch (Exception)
                {
                    Console.WriteLine("DANGER TEXT DOCUMENT SELECTED");
                    Console.WriteLine("Enter |fileName|.|extension Ex: 'test_text.txt'");
                }
            }

            List<string> output = new List<string>();
            output = CreateNGrams(input, userNumInt);

            Nouns noun = new Nouns();
            noun.Read();

            PrintAnagramToTextDoc(output, noun.nounIndexDict, noun.nounDataDict);

            Console.WriteLine("\nProgram has finished running. Please check file folders for output file 'output.txt'");
            Console.ReadLine();
        }

        public static List<string> StripInput(string file)
        {
            string[] wordsAnagram = null;
            string[] anagram = System.IO.File.ReadAllLines(file);

            foreach (var item in anagram)
            {
                wordsAnagram = item.Split(' ');
            }

            List<string> listString = new List<string>();
            foreach (var item in wordsAnagram)
            {
                //Need to strip off the period
                char[] MyChar = { '.' };
                string NewString = item.TrimEnd(MyChar);
                listString.Add(NewString);
            }

            return listString;
        }

        public static List<string> CreateNGrams(List<string> strList, int numAnagram)
        {
            List<string> compiledAnagrams = new List<string>();
            int minus = numAnagram - 1;
            for (int i = 0; i < strList.Count- minus; i++)
            {
                string gram = "";
                for (int j = 0; j < numAnagram; j++)
                {
                    gram += strList[i+j];
                    if (j != numAnagram - 1)
                        gram += "_";
                }
                compiledAnagrams.Add(gram);
            }

            return compiledAnagrams;
        }

        public static void PrintAnagramToTextDoc(List<string> strList, Dictionary<string, string> index, Dictionary<string, string> data)
        {
            //Clear Sheet
            File.WriteAllText(@"output.txt", String.Empty);
            //Read in Dicionary
            foreach (string item in strList)
            {
                //Next find Ngrams and print them
                string value = findDictionaryValues(index, item.ToLower());

                string text = findMultipleDictionaryValues(data, value);
                using (System.IO.StreamWriter file =
                    new System.IO.StreamWriter(@"output.txt", true))
                {
                    file.WriteLine(item + ", " + text);
                }
            }
        
        }

        public static string findDictionaryValues(Dictionary<string, string> dict, string token)
        {
            List<string> keyList = new List<string>();
            string value = null;

            if (token != null)
            {
                if (dict.ContainsKey(token))
                {
                    value = dict[token];
                }
            }

            return value;
        }
        public static string findMultipleDictionaryValues(Dictionary<string, string> dict, string token)
        {
            string value = null;
            string valueWithAND = null;

            if (token != null)
            {
                string[] multiValues = token.Split(',');
                for (int i = 0; i < multiValues.Length; ++i)
                {
                    if (dict.ContainsKey(multiValues[i]))
                    {

                        value += dict[multiValues[i]];
                        if (multiValues.Length - 1 != i)
                            value += "<< or >>";
                    }
                }

                if (value != null)
                {
                    foreach (char c in value)
                    {
                        if (c == ';')
                            valueWithAND += "<< and >>";
                        else
                            valueWithAND += c;
                    }
                }
            }

            return valueWithAND;
        }

    }
}
