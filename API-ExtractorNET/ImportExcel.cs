using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Office.Interop.Excel;
using Application = Microsoft.Office.Interop.Excel.Application;

namespace ApiExtractor
{
    class ImportExcel
    {
        public static ArrayList importSymbolsFromExcel(String path)
        {
            //importing excel file with id symbols values as list
            ArrayList allSymbols = new ArrayList();
            try
            {

                Microsoft.Office.Interop.Excel.Application xlApp = new Application();
                if (xlApp == null)
                {
                    MessageBox.Show("Excel is not properly installed!!");
                }

                Workbook worKbooK = xlApp.Workbooks.Add(path);
                Sheets excelSheets = worKbooK.Worksheets;
                string currentSheet = "Sheet1";
                Worksheet excelWorksheet = excelSheets.get_Item(currentSheet);
                Range UsedRange = excelWorksheet.UsedRange;
                int lastUsedRow = UsedRange.Row + UsedRange.Rows.Count - 1;

                foreach (Range r in UsedRange.Cells) //range1.Cells represents all the columns/rows
                {
                    var cellValue = r.ToString().ToLower();
                    if (!cellValue.Equals("symbol"))
                    {
                        allSymbols.Add(cellValue);
                    }
                }
                
            }
            catch (Exception ex)
            {

            }
            return allSymbols;
        }
    }
}