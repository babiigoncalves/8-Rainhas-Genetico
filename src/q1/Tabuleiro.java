package q1;

public class Tabuleiro {

	public int[] tabuleiro;
	public int custo;
	public Tabuleiro pai;
	public boolean mutacao = false;
	
	public Tabuleiro(){
		super();
	}

	public Tabuleiro(int[] tabuleiro, Tabuleiro pai) {
		super();
		this.tabuleiro = tabuleiro;
		this.pai = pai;
	}

	public int[] getTabuleiro() {
		return tabuleiro;
	}

	public void setTabuleiro(int[] tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	public int getCusto() {
		return custo;
	}

	public void setCusto(int custo) {
		this.custo = custo;
	}

	public Tabuleiro getPai() {
		return pai;
	}

	public void setPai(Tabuleiro pai) {
		this.pai = pai;
	}
	
	
	
	public boolean isMutacao() {
		return mutacao;
	}

	public void setMutacao(boolean mutacao) {
		this.mutacao = mutacao;
	}

	public boolean equals(Tabuleiro tab){
		boolean retorno = true;
		int[] temp = tab.getTabuleiro();
		
		for(int i = 0; i < 8;i++){
			if(temp[i] != this.tabuleiro[i]){
				retorno = false;
				break;
			} 
		}
		return retorno;
	}
	
	public int comparar(Tabuleiro tabuleiroComp) {
		int custoRetorno = 0;

		if (this.custo > tabuleiroComp.getCusto()) {
			custoRetorno = 1;
		} else if (this.custo == tabuleiroComp.getCusto()) {
			custoRetorno = 0;
		} else {
			custoRetorno = -1;
		}

		return custoRetorno;
	}

}
