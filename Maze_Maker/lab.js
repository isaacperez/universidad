
function crea_laberinto(MaxF, MaxC, numR)
{
	// Atributos Globlales
	
	var MaxFila = MaxF;
	var MaxColumna = MaxC;

	var Laberinto = new Array();

	// Inicializacion a 0 del array del laberinto
	for (fila=0;fila<MaxFila;fila++) {

		Laberinto[fila] = new Array();
		
		for (columna=0;columna<MaxColumna;columna++) {
			
			if( fila == 0 || fila == (MaxFila-1) ){
				
				Laberinto[fila][columna]=1;
			}	
			else if( columna == 0 || columna == (MaxColumna-1) ){
	
				Laberinto[fila][columna]=1;
				
			}	
			else {
				Laberinto[fila][columna]=0;
			}
		
		}
	
	}

	// Recorrido para crear laberinto
	
	var NumRecorridos = numR;
	
	var movAnterior = 0;
	
	for (recorrido = 0; recorrido < NumRecorridos; recorrido++){
		
		// Recorrido de Abajo a Arriba
		
		var posFila = MaxFila-1;		// Fila por encima del pasillo
		var posColumna = Math.floor((Math.random() * (MaxColumna-2)) + 1);		// Columna desde 1 hasta (MaxColumna-2)
		
		var limiteSuperior = 0;
		
		while ( posFila > (limiteSuperior + 1) )
		{
			
			var movimiento = Math.floor((Math.random() * 3) + 1);	// Movimientos = 1: Arriba 2: Izquierda 3: Derecha
			
			if( movimiento == 1) // Movimiento hacia arriba
			{
				posFila--;
				Laberinto[posFila][posColumna] = 1;
				posFila--;
				Laberinto[posFila][posColumna] = 1;
				
				movAnterior = 0;
				
			}
			
			else if ( (movimiento == 2)  && ( (movAnterior == 0) || (movAnterior == -1) ) && (posColumna > 1) )
			{
				posColumna--;
				Laberinto[posFila][posColumna] = 1;
				movAnterior = -1;				
			}
			
			else if( (movimiento == 3) && ( (movAnterior == 0) || (movAnterior == 1) ) && ( posColumna < (MaxColumna-2) ) )
			{
				posColumna++;
				Laberinto[posFila][posColumna] = 1;
				movAnterior = 1;	
			}
		}	
		
		// Recorrido de Izquierda a Derecha
		
		var posFila = Math.floor((Math.random() * (MaxFila-2)) + 1);		// Fila por encima del pasillo
		var posColumna = 0;		// Columna desde 1 hasta (MaxColumna-2)
		
		var limiteLateral = MaxColumna-1;
		
		while ( posColumna < (limiteLateral-1) )
		{
			
			var movimiento = Math.floor((Math.random() * 3) + 1);	// Movimientos = 1: Derecha 2: Arriba 3: Abajo
			
			if( movimiento == 1) // Movimiento hacia la derecha
			{
				posColumna++;
				Laberinto[posFila][posColumna] = 1;
				posColumna++;
				Laberinto[posFila][posColumna] = 1;
		
				movAnterior = 0;
				
			}
			
			else if ( (movimiento == 2)  && ( (movAnterior == 0) || (movAnterior == 1) ) && (posFila > 1) )
			{
				posFila--;
				Laberinto[posFila][posColumna] = 1;
				movAnterior = 1;				
			}
			
			else if( (movimiento == 3) && ( (movAnterior == 0) || (movAnterior == -1) ) && ( posFila < (MaxFila - 2) ) )
			{
				posFila++;
				Laberinto[posFila][posColumna] = 1;
				movAnterior = -1;	
			}
		}
		
	}
	
	return Laberinto;
	
}

function etiquetado(MaxFila, MaxColumna, Laberinto)
{
	
	// Etiquetado
	Pesos = [8,4,2,1];
	
	for (fila=0;fila<MaxFila;fila++) {
		
		for (columna=0;columna<MaxColumna;columna++) {
			
			if ( Laberinto[fila][columna] != 0)		// Miro sus vecinos
			{
				
				Vecinos = [ 0, 0, 0, 0];
				
				if( columna != 0 ) // Puedo mirar a la izquierda
				{
					if(Laberinto[fila][columna-1] != 0)
					{
						Vecinos[0] =  1;
					}
				}
				
				if( columna != (MaxColumna-1) )	// Puedo mirar a la derecha
				{
					if(Laberinto[fila][columna+1] != 0)
					{
						Vecinos[1] = 1;
					}
					
					
				}
				
				if( fila != 0 ) // Puedo mirar a arriba
				{
					if( Laberinto[fila-1][columna] !=0 )
					{
						Vecinos[2] = 1;
					}
					
				}
				
				if( fila != (MaxFila-1) )		// Puedo mirar abajo
				{
					if(Laberinto[fila+1][columna]!=0)
					{
						Vecinos[3] = 1;
					}	
				}
				
				valor = Vecinos[0]*Pesos[0] + Vecinos[1]*Pesos[1] + Vecinos[2]*Pesos[2] + Vecinos[3]*Pesos[3];
				
				Laberinto[fila][columna] = valor;
				
			}
		}
	}
	
	return Laberinto;
	
}
