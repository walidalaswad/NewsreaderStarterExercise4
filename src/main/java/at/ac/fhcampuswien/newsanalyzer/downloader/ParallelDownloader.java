package at.ac.fhcampuswien.newsanalyzer.downloader;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDownloader extends Downloader{

    private int numWorkers = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(numWorkers);

    @Override
    public int process(List<String> urls) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        int count = 0;
        for(String url : urls) {
            Future<String> future = pool.submit( () -> saveUrl2File(url));
            if(future.get() != null) count++;
        }
        long endTime = System.currentTimeMillis();

        long timeElapsed = endTime - startTime;

        System.out.println("Execution time in milliseconds at parallel download: " + timeElapsed);

        return count;
    }
}
