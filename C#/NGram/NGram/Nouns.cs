using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace NGram
{
    public class Nouns
    {
        public Dictionary<string, string> nounDataDict = new Dictionary<string, string>();
        public Dictionary<string, string> nounIndexDict = new Dictionary<string, string>();
        public void Read()
        {
            try
            {
                string[] lines2 = System.IO.File.ReadAllLines("NounsIndex.txt");
                foreach (string item in lines2)
                {
                    string[] words = item.Split('|');
                    nounIndexDict.Add(words[0], words[1]);

                }

                //Read in Data
                string[] lines = System.IO.File.ReadAllLines("NounsData.txt");
                foreach (string item in lines)
                {
                    string[] words = item.Split('|');
                    nounDataDict.Add(words[0], words[1]);
                }
                //Read in index

            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }


        }
    }
}
