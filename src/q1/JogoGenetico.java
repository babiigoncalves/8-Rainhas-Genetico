package q1;

//Barbara Goncalves
import java.util.ArrayList;
import java.util.Random;

public class JogoGenetico {

	public ArrayList<Tabuleiro> populacao;

	public JogoGenetico() {
		super();
		this.populacao = new ArrayList<Tabuleiro>();
	}

	public void buscaSolucao() {

		boolean solucao = false;
		int itMax = 0;
		Tabuleiro tab = null;

		this.gerarPopulacaoInicial();
		while (!solucao && itMax < 50000) {
			tab = this.checaSolucao();
			if (tab != null) {
				solucao = true;
			} else {
				// expandir filhos
				this.expandirFilhos(this.populacao);
			}
			itMax++;
			System.out.println(itMax);
		}

		if (!solucao) {
			System.out.println("Solucao nao encontrada!");
		} else {
			System.out.println("Achou em : " + itMax + " iteracoes.");
			this.imprimirSolucao(tab);
		}

	}

	public void expandirFilhos(ArrayList<Tabuleiro> tabPai) {
		Random r = new Random();
		int randomSort = 0;
		Tabuleiro pai1 = null;
		Tabuleiro pai2 = null;
		ArrayList<Tabuleiro> filhosGerados = new ArrayList<Tabuleiro>();
		do {

			pai1 = this.sorteio();
			int cont = 0;
			do {
				pai2 = this.sorteio();

				if (pai2.equals(pai1)) {
					cont++;
				}
				if (cont > 4) {
					break;
				}
			} while (pai2.equals(pai1));
			randomSort++;
			filhosGerados.addAll(this.crossOver(pai1, pai2));
		} while (randomSort < 2);
		// mutacao

		randomSort = r.nextInt(40);

		if (randomSort < filhosGerados.size()) {
			Tabuleiro aux = filhosGerados.get(randomSort);
			filhosGerados.remove(randomSort);
			this.mutacao(aux);
			filhosGerados.add(aux);
		}

		this.populacao = new ArrayList<Tabuleiro>();
		this.populacao.addAll(filhosGerados);

	}

	public Tabuleiro checaSolucao() {
		Tabuleiro solucao = null;
		for (Tabuleiro tab : this.populacao) {
			if (tab.getCusto() == 0) {
				solucao = tab;
			}
		}
		return solucao;
	}

	// Métodos de conversao inteiro para bit e bit para inteiro

	public int[] converteIntBit(int inteiro) {
		int[] bit = new int[3];

		for (int i = 0, pos = 2; i < 3; i++, pos--) {
			if (inteiro % 2 == 0) {
				bit[pos] = 0;
			} else {
				bit[pos] = 1;
			}
			inteiro /= 2;
		}
		return bit;
	}

	public int converteBitInteiro(int[] bit) {
		int inteiro = 0;
		for (int i = 0, pos = 2; i < 3; i++, pos--) {
			if (bit[i] > 0) {
				inteiro += Math.pow(2, pos);
			}
		}
		return inteiro;
	}

	// Gerar a populacao inicial

	public void gerarPopulacaoInicial() {
		Tabuleiro aux = null;
		int[] vetorAux = null;

		Random rand = new Random();

		for (int i = 0; i < 2; i++) {
			vetorAux = new int[8];

			for (int j = 0; j < 8; j++) {
				vetorAux[j] = rand.nextInt(8);
			}
			aux = new Tabuleiro(vetorAux, null);
			this.populacao.add(aux);

		}
		// inverter os bits;
		this.inverterBits();
		// calcular custo;
		this.qtdAtaques(this.populacao);

	}

	public void verificaAtaquesPosicao(Tabuleiro tab) {
		int posicao = 0;
		int qtdAtaques = 0;
		int[] vetor = tab.getTabuleiro();

		while (posicao < 8) {
			if ((posicao + 1) <= 7) {
				int proximaPosicao = posicao;
				proximaPosicao++;

				while (proximaPosicao < 8) {
					if (Math.abs(posicao - proximaPosicao) == Math
							.abs(vetor[posicao] - vetor[proximaPosicao])) {
						qtdAtaques++;
					}
					proximaPosicao++;
				}
			}
			posicao++;
		}

		for (int i = 0; i < 8; i++) {
			for (int j = i + 1; j < 8; j++) {
				if (vetor[i] == vetor[j]) {
					qtdAtaques++;
				}
			}
		}

		 tab.setCusto(qtdAtaques);
		//tab.custo = qtdAtaques;
		// return qtdAtaques;
	}

	public void qtdAtaques(ArrayList<Tabuleiro> listaTab) {

		for (Tabuleiro tab : listaTab) {
			this.verificaAtaquesPosicao(tab);
		}
	}

	public void inverterBits() {
		int[] temp;
		Tabuleiro tabAux;
		Tabuleiro tabNovo;
		ArrayList<Tabuleiro> listaTemp = new ArrayList<Tabuleiro>();

		for (int i = 0; i < 2; i++) {
			tabAux = this.populacao.get(i);

			temp = this.wololo(tabAux.getTabuleiro());

			for (int j = 0; j < 24; j++) {
				if (temp[j] == 0) {
					temp[j] = 1;
				}
			}
			tabNovo = new Tabuleiro(this.desconverter(temp), null);
			listaTemp.add(tabNovo);
		}
		this.populacao.addAll(listaTemp);
	}

	public int[] wololo(int[] tab) {
		int[] convertido = new int[24];
		int[] temp;

		for (int i = 0, j = 0; i < 8; i++) {
			temp = this.converteIntBit(tab[i]);

			for (int k = 0; k < 3; k++) {
				convertido[j] = temp[k];
				j++;
			}
		}
		return convertido;

	}

	public int[] desconverter(int[] convertido) {
		int[] desconvertido = new int[8];
		int[] temp;

		for (int i = 0, j = 0; i < 8; i++) {
			temp = new int[3];
			int posicao = 0;
			while (posicao < 3) {
				temp[posicao] = convertido[j];
				j++;
				posicao++;
			}

			desconvertido[i] = this.converteBitInteiro(temp);
		}
		return desconvertido;
	}

	private Tabuleiro sorteio() {
		Tabuleiro tabSelecionado = null;
		Tabuleiro e1, e2;
		Random rand = new Random();

		e1 = this.populacao.get(rand.nextInt(this.populacao.size()));
		e2 = this.populacao.get(rand.nextInt(this.populacao.size()));
		 //tabSelecionado = this.populacao .get(rand.nextInt(this.populacao.size()));

		if (e1.getCusto() < e2.getCusto()) {
			tabSelecionado = e1;
		} else {
			tabSelecionado = e2;
	}
		return tabSelecionado;
	}

	public ArrayList<Tabuleiro> crossOver(Tabuleiro pai1, Tabuleiro pai2) {
		Random r = new Random();
		int[] genePai1, genePai2;
		int pontoCorte = r.nextInt(24);
		int temp;
		Tabuleiro tabAux = null;
		ArrayList<Tabuleiro> filhosGerados = new ArrayList<Tabuleiro>();

		genePai1 = this.wololo(pai1.getTabuleiro());
		genePai2 = this.wololo(pai2.getTabuleiro());

		for (int i = pontoCorte; i < 24; i++) {
			temp = genePai1[i];
			genePai1[i] = genePai2[i];
			genePai2[i] = temp;
		}

		tabAux = new Tabuleiro(this.desconverter(genePai1), null);
		filhosGerados.add(tabAux);
		tabAux = new Tabuleiro(this.desconverter(genePai2), null);
		filhosGerados.add(tabAux);
		
		this.qtdAtaques(filhosGerados);

		return filhosGerados;
	}

	public void mutacao(Tabuleiro tabFilho) {
		Random r = new Random();
		int bitRandom;
		int mutRandom;
		int[] bits;
		mutRandom = r.nextInt(8);
		bits = this.converteIntBit(mutRandom);
		int[] tabuleiro = tabFilho.getTabuleiro();

		bitRandom = r.nextInt(3);
		if (bits[bitRandom] == 0) {
			bits[bitRandom] = 1;
		} else {
			bits[bitRandom] = 0;
		}

		tabuleiro[mutRandom] = this.converteBitInteiro(bits);
		tabFilho.setMutacao(true);
		tabFilho.setTabuleiro(tabuleiro);
		this.verificaAtaquesPosicao(tabFilho);
	}

	/**
	 * Método que imprime a configuracao final onde nenhuma rainha se ataca
	 */
	public void imprimirSolucao(Tabuleiro tabuleiro) {
		System.out.print("[");
		for (int i = 0; i < 8; i++) {
			System.out.print(tabuleiro.getTabuleiro()[i]);
			if (i != 7) {
				System.out.print("|");
			}
		}
		System.out.print("]");
	}

	public void imprimirPopulacao(ArrayList<Tabuleiro> populacao) {
		for (Tabuleiro tab : populacao) {
			this.imprimirSolucao(tab);
		}
	}
}