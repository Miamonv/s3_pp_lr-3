package droids;

/** Інтерфейс для дій дрона, його "мозку" (стратегії поведінки)
 *  кожна дія реалізує метод execute(), який виконує певну поведінку
 */
public interface Action {
    String execute();
}