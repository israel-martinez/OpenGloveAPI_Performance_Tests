using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading;
using C_SharpTest.IO;
using CSharpTest.OpenGloveAPI_C_Sharp_HL;
using Xamarin.Forms;

namespace CSharpTest.OpenGloveLatencyTests
{
    public class LatencyTest
    {
        public string FolderName { get; set; }
        public string FileName { get; set; }
        public string StoragePath { get; set; }
        public string ColumnTitle { get; set; }
        public int Samples { get; set; }
        public OpenGlove OpenGloveDevice;
        public List<int> ActuatorRegions;
        public List<string> IntensitiesTurnOn;
        public List<string> IntensitiesTurnOff;


        public ICSV CSVWriter = DependencyService.Get<ICSV>();
        public Stopwatch stopwatch; // for capture elapsed time
        public TimeSpan ts;

        public volatile int MessageReceivedCounter = 0;
        public volatile int FlexorReceivedCounter = 0;
        public volatile int ActivateActuatorOnClientTimeTestCounter = 0;
        public volatile int ActivateActuatorOnArduinoTimeTestCounter = 0;
        public volatile int ActivateActuatorOnServerTimeTestCounter = 0;
        public int FlexorQuantity;
        public int ActuatorQuantity;
        public List<long> latencies = new List<long>();
        public List<long> latenciesActuatorsOnArduino = new List<long>();
        public List<long> latenciesActuatorsOnServer = new List<long>();
        public List<long> latenciesActuatorsOnClient = new List<long>();
        public volatile int ReadingCicleCounter = 0;
        public volatile int WritingCicleCounter = 0;
        public int MaxReadingCicle;


        public LatencyTest()
        {
        }

        public delegate void LatencyTestCompleted(string testType, string folderName, string fileName, string storagePath, string columnTitle,  int samples, int writingCicleCounter, int readingCicleCounter, int messageReceivedCounter);
        public event LatencyTestCompleted OnLatencyTestCompleted;



        public void StorageTest(string folderName, string fileName)
        {
            List<long> samplesList = new List<long>() { 10, 11, 12, 7, 0, 1 };
            CSVWriter.Write(folderName, fileName, "sample", samplesList);
        }

        //use flexors regions 0 to 9 (regions limited from OpenGlove Arduino Software control)
        public void OnFlexorValueReceived(int region, int value)
        {
            Debug.WriteLine($"[LatencyTest.OnFlexorValueReceived] latencies: {latencies.Count} ReadingCicleCounter: {ReadingCicleCounter}, MaxReadingCicle: {MaxReadingCicle}, region: {region}, value: {value}");
            if (FlexorQuantity == 1)
            {
                FlexorLatencyTestOfOneFlexor();
            }
            else
            {
                FlexorLatencyTestOfTwoOrMoreFlexors(region, value);
                
            }
        }

        public void FlexorLatencyTestOfOneFlexor()
        {
            if (latencies.Count < MaxReadingCicle)
            {
                if (ReadingCicleCounter == 0)
                {
                    stopwatch = new Stopwatch();
                    stopwatch.Start();
                    ReadingCicleCounter++;
                }
                else
                {
                    stopwatch.Stop();
                    ts = stopwatch.Elapsed;
                    latencies.Add(ts.Ticks * 100); // nanoseconds https://msdn.microsoft.com/en-us/library/system.datetime.ticks(v=vs.110).aspx
                    ReadingCicleCounter++;
                    stopwatch = new Stopwatch();
                    stopwatch.Start();
                }
                MessageReceivedCounter++;
                FlexorReceivedCounter++;
            }
            else if (latencies.Count <= MaxReadingCicle)
            {
                ReadingCicleCounter++;
                StoragePath = CSVWriter.Write(FolderName, FileName, ColumnTitle, latencies);
                OnLatencyTestCompleted?.Invoke("flexors&IMU", FolderName, FileName, StoragePath, ColumnTitle, Samples, WritingCicleCounter, ReadingCicleCounter, MessageReceivedCounter);
            }
        }

        public void FlexorLatencyTestOfTwoOrMoreFlexors(int region, int value)
        {
            if (ReadingCicleCounter < MaxReadingCicle)
            {   //TODO need support for 1 flexor ... Start() Stop() stopWatch
                if (region == 0)
                {//start of new reading cicle, for one flexor, only par index is the first to start() stopWatch, impar index stop() stopWatch
                    stopwatch = new Stopwatch();
                    stopwatch.Start();
                }
                else if (region == FlexorQuantity - 1)
                {//end of one reading cicle
                    stopwatch.Stop();
                    ts = stopwatch.Elapsed;
                    latencies.Add(ts.Ticks * 100); // nanoseconds https://msdn.microsoft.com/en-us/library/system.datetime.ticks(v=vs.110).aspx
                    ReadingCicleCounter++;
                }
                MessageReceivedCounter++;
                FlexorReceivedCounter++;
            }
            else if (ReadingCicleCounter <= MaxReadingCicle)
            {
                ReadingCicleCounter++;
                StoragePath = CSVWriter.Write(FolderName, FileName, ColumnTitle, latencies);
                OnLatencyTestCompleted?.Invoke("flexors&IMU", FolderName, FileName, StoragePath, ColumnTitle, Samples, WritingCicleCounter, ReadingCicleCounter, MessageReceivedCounter);
            }
        }

        public void OnAllIMUValues(float ax, float ay, float az, float gx, float gy, float gz, float mx, float my, float mz)
        {
            MessageReceivedCounter++;
        }

        public void FlexorAndIMUTest(OpenGlove openGloveDevice, string folderName, string fileName, string columnTitle, int samples, int flexorQuantity)
        {
            this.FolderName = folderName;
            this.FileName = fileName;
            this.ColumnTitle = columnTitle;
            this.Samples = samples;

            openGloveDevice.Communication.OnFlexorValueReceived += this.OnFlexorValueReceived;
            openGloveDevice.Communication.OnAllIMUValuesReceived += this.OnAllIMUValues;
            this.FlexorQuantity = flexorQuantity;
            this.MaxReadingCicle = Samples;
        }

        public void OnActivateActuatorsTimeTestOnArduinoReceived2(long microSeconds)
        {
            Debug.WriteLine($"[OnArduinoActuatorTest]: {ActivateActuatorOnArduinoTimeTestCounter}");
            if (this.ActivateActuatorOnArduinoTimeTestCounter < this.Samples)
            {
                latenciesActuatorsOnArduino.Add(microSeconds * 1000); //add nanoseconds (1 micro second = 1000 nanoseconds)
                this.ActivateActuatorOnArduinoTimeTestCounter++;

                if (this.ActivateActuatorOnArduinoTimeTestCounter < this.Samples)
                    ActivateActuatorsOnWebSocketClient(); //send a new Command Message for activate actuators

                if (this.ActivateActuatorOnArduinoTimeTestCounter == this.Samples)
                {
                    Debug.WriteLine("[OnArduinoActuatorTest]: finalized");
                    OnLatencyTestCompleted?.Invoke("actuators", FolderName, FileName, StoragePath, ColumnTitle, Samples, WritingCicleCounter, ReadingCicleCounter, MessageReceivedCounter);
                }
            }
        }

        public void OnActivateActuatorsTimeTestOnServerReceived2(long nanoSeconds)
        {
            Debug.WriteLine($"[OnServerActuatorTest]:\t {ActivateActuatorOnServerTimeTestCounter}");
            if (this.ActivateActuatorOnServerTimeTestCounter < this.Samples)
            {
                latenciesActuatorsOnServer.Add(nanoSeconds);
                this.ActivateActuatorOnServerTimeTestCounter++;

                if (this.ActivateActuatorOnServerTimeTestCounter == this.Samples)
                {
                    Debug.WriteLine("[OnServerActuatorTest]:\t finalized");
                    OnLatencyTestCompleted?.Invoke("actuators", FolderName, FileName, StoragePath, ColumnTitle, Samples, WritingCicleCounter, ReadingCicleCounter, MessageReceivedCounter);
                }
            }
        }

        public void ActuatorsTest2(OpenGlove openGloveDevice, string folderName, string fileName, string columnTitle, int samples, int actuatorQuantity, List<int> actuatorRegions)
        {
            this.FolderName = folderName;
            this.FileName = fileName;
            this.ColumnTitle = columnTitle;
            this.Samples = samples;
            this.OpenGloveDevice = openGloveDevice;
            this.ActuatorRegions = actuatorRegions; 
            IntensitiesTurnOn = GenerateActuatorIntensitiesList(actuatorQuantity, "255");
            IntensitiesTurnOff = GenerateActuatorIntensitiesList(actuatorQuantity, "0");
            openGloveDevice.Communication.OnActivateActuatorsTimeTestOnArduinoReceived += this.OnActivateActuatorsTimeTestOnArduinoReceived2;
            openGloveDevice.Communication.OnActivateActuatorsTimeTestOnServerReceived += this.OnActivateActuatorsTimeTestOnServerReceived2;

            ActivateActuatorsOnWebSocketClient();
        }

        public void RemoveActuatorsEvents()
        {
            OpenGloveDevice.Communication.OnActivateActuatorsTimeTestOnArduinoReceived -= this.OnActivateActuatorsTimeTestOnArduinoReceived2;
            OpenGloveDevice.Communication.OnActivateActuatorsTimeTestOnServerReceived -= this.OnActivateActuatorsTimeTestOnServerReceived2;

        }

        public void RemoveFlexorsAndIMUEvents()
        {
            OpenGloveDevice.Communication.OnFlexorValueReceived -= this.OnFlexorValueReceived;
            OpenGloveDevice.Communication.OnAllIMUValuesReceived -= this.OnAllIMUValues;
        }

        public void ActivateActuatorsOnWebSocketClient()
        {
            Debug.WriteLine($"[OnClientActuatorTest]:\t {ActivateActuatorOnClientTimeTestCounter}");
            if (this.ActivateActuatorOnClientTimeTestCounter < this.Samples)
            {
                if (ActivateActuatorOnClientTimeTestCounter % 2 == 0)
                {
                    stopwatch = new Stopwatch();
                    stopwatch.Start();

                    OpenGloveDevice.ActivateActuatorsTimeTest(ActuatorRegions, IntensitiesTurnOn);

                    stopwatch.Stop();
                    ts = stopwatch.Elapsed;
                    latenciesActuatorsOnClient.Add(ts.Ticks * 100); // nanoseconds https://msdn.microsoft.com/en-us/library/system.datetime.ticks(v=vs.110).aspx
                }
                else
                {
                    stopwatch = new Stopwatch();
                    stopwatch.Start();

                    OpenGloveDevice.ActivateActuatorsTimeTest(ActuatorRegions, IntensitiesTurnOff);

                    stopwatch.Stop();
                    ts = stopwatch.Elapsed;
                    latenciesActuatorsOnClient.Add(ts.Ticks * 100); // nanoseconds https://msdn.microsoft.com/en-us/library/system.datetime.ticks(v=vs.110).aspx
                }

                ActivateActuatorOnClientTimeTestCounter++;

                if (this.ActivateActuatorOnClientTimeTestCounter == this.Samples)
                {
                    Debug.WriteLine("[OnClientActuatorTest]:\t finalized");
                    OnLatencyTestCompleted?.Invoke("actuators", FolderName, FileName, StoragePath, ColumnTitle, Samples, WritingCicleCounter, ReadingCicleCounter, MessageReceivedCounter);
                }
            }
        }

        public bool IsActuatorTestComplete()
        {
            return (ActivateActuatorOnArduinoTimeTestCounter == Samples) && (ActivateActuatorOnServerTimeTestCounter == Samples && (ActivateActuatorOnClientTimeTestCounter == Samples));
        }

        public List<string> GenerateActuatorIntensitiesList(int quantity, string intensity)
        {
            List<string> intensities = new List<string>();

            for (int i = 0; i < quantity; i++)
            {
                intensities.Add(intensity);
            }

            return intensities;
        }

        public void GenerateCSVFileForActuatorsLatencyTest()
        {
            CalculateTotalActuatorLatenciesTimeTest();
            StoragePath = CSVWriter.Write(FolderName, FileName, ColumnTitle, latencies);
            OnLatencyTestCompleted?.Invoke("actuators", FolderName, FileName, StoragePath, ColumnTitle, Samples, WritingCicleCounter, ReadingCicleCounter, MessageReceivedCounter);
        }

        public void CalculateTotalActuatorLatenciesTimeTest()
        {
            Debug.WriteLine($"latenciesOnClient.Count: {latenciesActuatorsOnClient.Count}, latenciesOnServer.Count: {latenciesActuatorsOnServer.Count}, latenciesOnArduino.Count: {latenciesActuatorsOnArduino.Count}");
            for (int i = 0; i < Samples; i++)
            {
                latencies.Add(latenciesActuatorsOnClient[i] + latenciesActuatorsOnServer[i] + latenciesActuatorsOnArduino[i]);
            }
        }
    }
}
