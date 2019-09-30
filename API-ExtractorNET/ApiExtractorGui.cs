using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Net;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;
using System.Threading;
using System.Collections;

namespace ApiExtractor
{
    public partial class ApiExtractorGui : Form
    {
        public static string pathInputField = "";
        private string file;
        private bool logobuttonWasClicked = false;
        Random rnd = new Random();
        private string LOGOS_ENDPOINT = "https://storage.googleapis.com/iex/api/logos/";
        private string[] companyNames = {"AAPL", "GOOGL", "BAM", "XOM", "BUD", "INTC", "C", "FB",
            "ORCL", "BAC", "MSFT", "HD", "PFE", "PG", "JPM", "AMZN", "UNH", "T", "VZ",
            "WMT", "WFC", "CHL", "NVS", "JNJ", "TSM", "CVX", "BABA", "V"}; // input from file is TBD

        public ApiExtractorGui()
        {
            InitializeComponent();
        }

        private void GetStreamProgress(object sender, EventArgs e)
        {
            label1.Text = "Stream progress";
        }

        private void OpenFileDialog(object sender, CancelEventArgs e)
        {
            
        }

        private void browseButton(object sender, EventArgs e)
        {
            DialogResult result = openFile.ShowDialog(); // Show the dialog.
            if (result == DialogResult.OK) // Test result.
            {
                file = openFile.FileName;
                textBox1.Text = file;
                /*try
                {
                    string text = File.ReadAllText(file);
                }
                catch (IOException)
                {
                }*/
            }
            Console.WriteLine(result); // <-- For debugging use.
        }

        private void GetTextChanged(object sender, EventArgs e)
        {

        }

        private void GetPictureBox(object sender, EventArgs e)
        {            
            System.Diagnostics.Process.Start(LOGOS_ENDPOINT + companyNames[rnd.Next(companyNames.Length)].ToUpper() + ".png");
        }

        private void LoadGui(object sender, EventArgs e)
        {
            WebClient myWebClient = new WebClient();
            byte[] imageData = myWebClient.DownloadData("https://storage.googleapis.com/iex/api/logos/AAPL.png");
            label4.Text = String.Format("Logo URL: {0}", "https://storage.googleapis.com/iex/api/logos/AAPL.png");
            MemoryStream stream = new MemoryStream(imageData);
            try
            {
                Image logo = Image.FromStream(stream);
                pictureBox1.Size = new Size(logo.Width, logo.Height);
                pictureBox1.Image = logo;
                stream.Close();
            }
            catch (System.ArgumentException ex)
            {

            }
        }

        private void GetPartnerLogo(object sender, EventArgs e)
        {
            for (int i = 0; i < companyNames.Length; i++)
            {
                if (logobuttonWasClicked == false || progressBar1.Value == progressBar1.Maximum)
                {
                    label2.Text = "API Extractor";
                    label2.Update();
                    progressBar1.Value = progressBar1.Minimum;
                    progressBar1.Update();
                    logobuttonWasClicked = true;
                }

                if (logobuttonWasClicked == true && progressBar1.Value != progressBar1.Maximum)
                {
                    label2.Text = "Extracting...";
                    while (progressBar1.Value < progressBar1.Maximum)
                    {
                        progressBar1.Value++;
                    }
                    logobuttonWasClicked = false;
                }

                label4.Text = String.Format("Logo URL: {0}", LOGOS_ENDPOINT + companyNames[rnd.Next(i)].ToUpper() + ".png");
                
                WebClient myWebClient = new WebClient();
                byte[] imageData = myWebClient.DownloadData(LOGOS_ENDPOINT + companyNames[rnd.Next(i)].ToUpper() + ".png");
                MemoryStream stream = new MemoryStream(imageData);
                try
                {
                    Image logo = Image.FromStream(stream);
                    pictureBox1.Size = new Size(logo.Width, logo.Height);
                    pictureBox1.Image = logo;
                    stream.Close();
                }
                catch(System.ArgumentException ex)
                {
                   
                }
            }
        }

        private void GetProgress(object sender, EventArgs e)
        {
            progressBar1.Minimum = 0;
            progressBar1.Maximum = 100;
            progressBar1.Value = 100;

            int percent = (int)(((double)(progressBar1.Value - progressBar1.Minimum) /
            (double)(progressBar1.Maximum - progressBar1.Minimum)) * 100);

            using (Graphics gr = progressBar1.CreateGraphics())
            {
                gr.DrawString(percent.ToString() + "% Loading Progress",

                    SystemFonts.DefaultFont,

                    System.Drawing.Brushes.Black,

                    new PointF(progressBar1.Width / 2 - (gr.MeasureString(percent.ToString() + "% Loading Progress",

                        SystemFonts.DefaultFont).Width / 2.0F),

                    progressBar1.Height / 2 - (gr.MeasureString(percent.ToString() + "% Loading Progress",

                        SystemFonts.DefaultFont).Height / 2.0F)));
            }
        }

        private void GetLogoUrl(object sender, EventArgs e)
        {

        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

         //importing id symbols and putting it to list of strings
        private ArrayList listOfStockSymbols()
        {
            pathInputField = textBox1.Text;
            ArrayList listOfStockSymbols = ArrayList.Synchronized(ImportExcel.importSymbolsFromExcel(pathInputField));
            return listOfStockSymbols;
        }

        private Object[][] addingValuesToArrays(ArrayList listOfStockSymbols)
        {

            return null;
        }
    }
}
