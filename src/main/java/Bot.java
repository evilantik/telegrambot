import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bot extends TelegramLongPollingBot {

    private final static Logger logger = Logger.getLogger(Bot.class);

    private RequestHandler requestHandler = new RequestHandler();

    private Player lastPlayer;

    public void onUpdateReceived(Update update) {

        // переменные из апдейта для использования
        String text = update.getMessage().getText();
        String userName = update.getMessage().getChat().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        String chatIdForReply = update.getMessage().getChatId().toString();

        // если /start - приветствие + команды
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (text) {
                case "/start":
                    sendMsg(chatIdForReply,
                            "Привет, я могу сказать что-то о последних катках в дотан кое-кого (команда */last | /l*)," +
                                    "или проверить их на он-лайн (команда */check | /c*)");
                    sendButtonsWithCommands(chatIdForReply);
                    break;
                case "/last":
                case "/l":
                    sendButtonsWithNicksForLast(chatIdForReply);
                    break;
                case "/check":
                case "/c":
                    sendButtonsWithNicksForCheck(chatIdForReply);
                    break;
            }
        }

        // паттерны для команд
        String lastPattern = "[/][l][ ].*";
        Pattern lastP = Pattern.compile(lastPattern);

        String checkPattern = "[/][c][ ].*";
        Pattern checkP = Pattern.compile(checkPattern);

        String numPattern = "[1-9]";
        Pattern numP = Pattern.compile(numPattern);

        //обработка кнопок
        Matcher lastMatcher = lastP.matcher(text);
        Matcher checkMatcher = checkP.matcher(text);
        Matcher numMatcher = numP.matcher(text);

        if (lastMatcher.find() || checkMatcher.find()) {
            try {
                Response response = requestHandler.process(text);
                lastPlayer = response.getPlayer();

                String message = response.toString();

                sendMsg(chatIdForReply, message);

                logger.info("Запрос по " + text + " от " + userName + " | " + firstName + " | " + lastName);

            } catch (IOException e) {
                logger.error(e.getStackTrace());
            }
        } else if (numMatcher.find()) {
            try {
                Response response = requestHandler.process(Integer.parseInt(text), lastPlayer);
                String message = response.toString();
                sendMsg(chatIdForReply, message);

                logger.info("Запрос по " + text + " от " + userName + " | " + firstName + " | " + lastName);

            } catch (IOException e) {
                logger.error(e.getStackTrace());
            }
        } else if (text.equals("/check all")) {
            try {
                Response response = requestHandler.process(Player.ANTON);
                StringBuilder stringBuilder = new StringBuilder(response.toString());
                stringBuilder.append("\n");
                response = requestHandler.process(Player.ANYA);
                stringBuilder.append(response.toString());
                stringBuilder.append("\n");
                response = requestHandler.process(Player.ARKAD);
                stringBuilder.append(response.toString());
                stringBuilder.append("\n");
                response = requestHandler.process(Player.MCHT);
                stringBuilder.append(response.toString());
                stringBuilder.append("\n");
                response = requestHandler.process(Player.MELKI);
                stringBuilder.append(response.toString());
                stringBuilder.append("\n");
                response = requestHandler.process(Player.VOLSH);
                stringBuilder.append(response.toString());

                sendMsg(chatIdForReply, stringBuilder.toString());

                logger.info("Запрос по " + text + " от " + userName + " | " + firstName + " | " + lastName);

            } catch (IOException e) {
                logger.error(e.getStackTrace());
            }
        }
    }

    private void sendButtonsWithCommands(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери команду:");


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/last");
        row.add("/check");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    private void sendButtonsWithNicksForLast(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери ник:");

        // Объект кнопок
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        // Сами кнопки \ лист рядов кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Создать один ряд кнопок
        KeyboardRow row = new KeyboardRow();
        // Кнопки
        row.add("/l Making Cookies Having Teas.");
        row.add("/l ATHLETE!");
        row.add("/l фкыруевмило");
        // Добавляем в лист
        keyboard.add(row);
        // Второя ряд
        row = new KeyboardRow();
        // Кнопки
        row.add("/l Milonov");
        row.add("/l ANYA II. BACK TO DOTA");
        row.add("/l Bird is the word");
        // Добавить в лист
        keyboard.add(row);
        // Добавить лист в объект
        keyboardMarkup.setKeyboard(keyboard);
        // Положить всё в сообщение
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }


    // некрасивое дублирование, но, кажется, красиво реализовать это не позволяет апи телеграма
    // ну или делать свою реализацию листа именно для кнопок
    private void sendButtonsWithNicksForCheck(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери ник:");

        // Объект кнопок
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        // Сами кнопки \ лист рядов кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Создать один ряд кнопок
        KeyboardRow row = new KeyboardRow();
        // Кнопки
        row.add("/c Making Cookies Having Teas.");
        row.add("/c ATHLETE!");
        row.add("/c фкыруевмило");
        // Добавляем в лист
        keyboard.add(row);
        // Второя ряд
        row = new KeyboardRow();
        // Кнопки
        row.add("/c Milonov");
        row.add("/c ANYA II. BACK TO DOTA");
        row.add("/c Bird is the word");
        // Добавить в лист
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("/check all");
        keyboard.add(row);
        // Добавить лист в объект
        keyboardMarkup.setKeyboard(keyboard);
        // Положить всё в сообщение
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }


    private synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "Botik";
    }

    public String getBotToken() {
        return System.getenv("telegramApiKey");
    }
}