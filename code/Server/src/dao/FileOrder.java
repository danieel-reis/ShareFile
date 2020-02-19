package br.com.ufop.daniel.d2dwifidirect.dao;

import java.io.File;

/**
 * Created by daniel on 19/11/17.
 */

public class FileOrder {

    public static void quickSortByLastModified(File[] vetor, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = separateByLastModified(vetor, inicio, fim);
            quickSortByLastModified(vetor, inicio, posicaoPivo - 1);
            quickSortByLastModified(vetor, posicaoPivo + 1, fim);
        }
    }

    private static int separateByLastModified(File[] vetor, int inicio, int fim) {
        File pivo = vetor[inicio];
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].lastModified() >= pivo.lastModified())
                i++;
            else if (pivo.lastModified() > vetor[f].lastModified())
                f--;
            else {
                File troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = pivo;
        return f;
    }

    public static void quickSortBySize(File[] vetor, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = separateBySize(vetor, inicio, fim);
            quickSortBySize(vetor, inicio, posicaoPivo - 1);
            quickSortBySize(vetor, posicaoPivo + 1, fim);
        }
    }

    private static int separateBySize(File[] vetor, int inicio, int fim) {
        File pivo = vetor[inicio];
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].length() < pivo.length())
                i++;
            else if (pivo.length() <= vetor[f].length())
                f--;
            else {
                File troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = pivo;
        return f;
    }
}
