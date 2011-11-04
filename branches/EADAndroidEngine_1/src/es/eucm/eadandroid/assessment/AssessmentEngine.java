package es.eucm.eadandroid.assessment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/*
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerConfigurationException;
 import javax.xml.transform.TransformerException;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.dom.DOMSource;
 import javax.xml.transform.stream.StreamResult;
 */
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import es.eucm.eadandroid.R;
import es.eucm.eadandroid.common.auxiliar.ReleaseFolders;
import es.eucm.eadandroid.common.data.assessment.AssessmentProfile;
import es.eucm.eadandroid.common.data.assessment.AssessmentRule;
import es.eucm.eadandroid.common.data.assessment.TimedAssessmentRule;
import es.eucm.eadandroid.common.loader.Loader;
import es.eucm.eadandroid.common.loader.incidences.Incidence;
import es.eucm.eadandroid.ecore.ECoreActivity;
import es.eucm.eadandroid.ecore.GameThread;
import es.eucm.eadandroid.ecore.ECoreActivity.ActivityHandlerMessages;
import es.eucm.eadandroid.ecore.control.FlagSummary;
import es.eucm.eadandroid.ecore.control.Game;
import es.eucm.eadandroid.ecore.control.TimerEventListener;
import es.eucm.eadandroid.ecore.control.TimerManager;
import es.eucm.eadandroid.ecore.control.VarSummary;
import es.eucm.eadandroid.ecore.control.functionaldata.FunctionalConditions;
import es.eucm.eadandroid.ecore.gui.GUI;
import es.eucm.eadandroid.homeapp.localgames.LocalGamesActivity.LGAHandlerMessages;
import es.eucm.eadandroid.res.resourcehandler.ResourceHandler;

/**
 * This engine stores the rules to be processed when the flags change in the
 * game, creating events that tell the process of the player in the game
 */
public class AssessmentEngine implements TimerEventListener {

	public static int STATE_STARTED = 1;

	public static int STATE_NONE = 0;

	public static int STATE_DONE = 2;

	/**
	 * Constants for the colors of the HTML reports
	 */
	private static String HTML_REPORT_COLOR_0 = "794910"; // Old value =#4386CE

	private static String HTML_REPORT_COLOR_1 = "F7D769"; // Old value =#C1D6EA

	private static String HTML_REPORT_COLOR_2 = "FFFFFF"; // Old value =#EEF6FE

	/**
	 * Current assessment profile
	 */
	private AssessmentProfile assessmentProfile;

	/**
	 * List of rules to be checked
	 */
	private List<AssessmentRule> assessmentRules;

	/**
	 * List of executed rules
	 */
	private List<ProcessedRule> processedRules;

	/**
	 * Structure of timed rules
	 */
	private HashMap<Integer, TimedAssessmentRule> timedRules;

	private String playerName;

	private int state;
	
	private String lastHTMLReport;

	/**
	 * Constructor
	 */
	public AssessmentEngine() {
		processedRules = new ArrayList<ProcessedRule>();
		timedRules = new HashMap<Integer, TimedAssessmentRule>();
		state = STATE_NONE;
	}

	/**
	 * Loads a set of assessment rules
	 * 
	 * @param assessmentPath
	 *            Path of the file containing the assessment data
	 */
	public void loadAssessmentRules(AssessmentProfile profile) {
		// if (assessmentPath != null && !assessmentPath.equals("")) {
		// assessmentProfile = Loader.loadAssessmentProfile(ResourceHandler
		// .getInstance(), assessmentPath, new ArrayList<Incidence>());
		if (profile != null) {
			assessmentProfile = profile;
			assessmentRules = new ArrayList<AssessmentRule>(assessmentProfile
					.getRules());

			FlagSummary flags = Game.getInstance().getFlags();
			VarSummary vars = Game.getInstance().getVars();
			for (String flag : assessmentProfile.getFlags()) {
				flags.addFlag(flag);
			}
			for (String var : assessmentProfile.getVars()) {
				vars.addVar(var);
			}
			// } else {
			// assessmentRules = new ArrayList<AssessmentRule>();
			// }

			// Iterate through the rules: those timed add them to the timer
			// manager
			for (AssessmentRule assessmentRule : assessmentRules) {
				if (assessmentRule instanceof TimedAssessmentRule) {
					TimedAssessmentRule tRule = (TimedAssessmentRule) assessmentRule;
					int id = TimerManager.getInstance().addTimer(
							tRule.getInitConditions(),
							tRule.getEndConditions(),
							tRule.isUsesEndConditions(), this);
					timedRules.put(new Integer(id), tRule);
				}
			}
			processRules();
		}
	}

	public static AssessmentProfile loadAssessmentProfile(String assessmentPath) {
		if (assessmentPath != null && !assessmentPath.equals("")) {
			AssessmentProfile assessmentProfile = Loader.loadAssessmentProfile(
					ResourceHandler.getInstance(), assessmentPath,
					new ArrayList<Incidence>());
			return assessmentProfile;
		}
		return null;
	}

	/**
	 * Process the rules, triggering them if necessary
	 */
	public void processRules() {
		int i = 0;

		if (assessmentRules != null) {
			// For every rule
			while (i < assessmentRules.size()) {

				// If it was activated, execute the rule
				if (isActive(assessmentRules.get(i))) {
					AssessmentRule oldRule = assessmentRules.remove(i);
					ProcessedRule rule = new ProcessedRule(oldRule, Game
							.getInstance().getTime());

					// System.out.println("Se cumple la regla "+
					// oldRule.getId());
					// Signal the LMS about the change

					processedRules.add(rule);
				}

				// Else, check the next rule
				else
					i++;
			}
		}
	}

	private static boolean isActive(AssessmentRule rule) {
		return new FunctionalConditions(rule.getConditions()).allConditionsOk();
	}

	/**
	 * Returns the timed rule indexed by key "i".
	 * 
	 * @param i
	 *            the key in the hash map
	 * @return the correct TimedAssessmentRule of timedRules
	 */
	public TimedAssessmentRule getTimedAssessmentRule(int i) {
		return timedRules.get(new Integer(i));
	}

	/**
	 * Generates a report file, in XML format
	 * 
	 * @param filename
	 *            File name of the report file
	 */
	/*
	 * public void generateXMLReport(String filename) { try { // Create the
	 * necessary elements for building the DOM DocumentBuilderFactory dbf =
	 * DocumentBuilderFactory.newInstance(); DocumentBuilder db =
	 * dbf.newDocumentBuilder(); Document doc = db.newDocument();
	 * 
	 * // Create the root element, "report" Element report =
	 * doc.createElement("report");
	 * 
	 * // For each processed rule for (ProcessedRule rule : processedRules) { //
	 * Create a new "processed-rule" node (DOM element), and link it // to the
	 * document Element processedRule = rule.getDOMStructure();
	 * doc.adoptNode(processedRule);
	 * 
	 * // Add the node report.appendChild(processedRule); }
	 * 
	 * // Add the report structure to the XML file doc.appendChild(report);
	 * 
	 * // Indent the DOM indentDOM(report, 0);
	 * 
	 * // Create the necessary elements for export the DOM into a XML file
	 * TransformerFactory tFactory = TransformerFactory.newInstance();
	 * Transformer transformer = tFactory.newTransformer();
	 * 
	 * // Create the output buffer, write the DOM and close it OutputStream fout
	 * = new FileOutputStream(filename); OutputStreamWriter writeFile = new
	 * OutputStreamWriter(fout, "UTF-8"); transformer.transform(new
	 * DOMSource(doc), new StreamResult( writeFile)); writeFile.close();
	 * 
	 * } catch (IOException exception) { exception.printStackTrace(); } catch
	 * (ParserConfigurationException exception) { exception.printStackTrace(); }
	 * catch (TransformerConfigurationException exception) {
	 * exception.printStackTrace(); } catch (TransformerException exception) {
	 * exception.printStackTrace(); } }
	 */
	/**
	 * Generates a report file, in HTML format
	 * 
	 * @param filename
	 *            File name of the report file
	 * @param minImportance
	 *            Importance value for filtering
	 */
	public void generateHTMLReport(String filename, int minImportance) {
		try {
			// Create the file to write
			PrintStream file = new PrintStream(new FileOutputStream(filename));

			// HTML tag
			file.println("<html>");
			
			// META tag for accents
			file.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");

			// Header
			file.print("<title>");
			file.print(Game.getInstance().getGameDescriptor().getTitle());
			file.println("</title>");
			// HTML tag
			file.println("<html>");

			// Header
			file.print("<title>");
			file.print(Game.getInstance().getGameDescriptor().getTitle());
			file.println("</title>");

			// Body and content table
			file.println("<body style=\"background: #" + HTML_REPORT_COLOR_0
					+ ";\">");
			file.println("<br/><br/>");
			file.println("<table align=\"center\" style=\"background : #"
					+ HTML_REPORT_COLOR_1 + "; border : 1px solid #000000;\">");
			file.println("<tr><td>");

			// Title
			file.print("<center><h3>");
			file.print(Game.getInstance().getGameDescriptor().getTitle());
			file.print(" report");
			file.println("</h3></center>");

			// Clear table
			file.println("<br/><br/>");
			file.println("<table align=\"center\" style=\"background : #"
					+ HTML_REPORT_COLOR_2 + "; border : 1px solid #000000\">");
			file.println("<tr><td>");

			// For each processed rule
			for (ProcessedRule rule : processedRules) {
				// First check the importance
				if (rule.getImportance() >= minImportance) {
					file.println(rule.getHTMLCode());
					file.println("<br/><br/>");
				}
			}

			// Close clear table
			file.println("</td></tr>");
			file.println("</table>");

			// Close table and body
			file.println("<br/><br/>");
			file.println("</td></tr>");
			file.println("</table>");
			file.println("</body>");

			// Close HTML
			file.println("</html>");

			// Close the file
			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indent the given DOM node recursively with the given depth
	 * 
	 * @param nodeDOM
	 *            DOM node to be indented
	 * @param depth
	 *            Depth of the current node
	 */
	private void indentDOM(Node nodeDOM, int depth) {
		// First of all, extract the document of the node, and the list of
		// children
		Document document = nodeDOM.getOwnerDocument();
		NodeList children = nodeDOM.getChildNodes();

		// Flag for knowing if the current node is empty of element nodes
		boolean isEmptyOfElements = true;

		int i = 0;
		// For each children node
		while (i < children.getLength()) {
			Node currentChild = children.item(i);

			// If the current child is an element node
			if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
				// Insert a indention before it, and call the recursive function
				// with the child (and a higher depth)
				nodeDOM.insertBefore(document.createTextNode("\n"
						+ getTab(depth + 1)), currentChild);
				indentDOM(currentChild, depth + 1);

				// Set empty of elements to false, and increase i (the new child
				// moves all children)
				isEmptyOfElements = false;
				i++;
			}

			// Go to next child
			i++;
		}

		// If this node has some element, add the indention for the closing tag
		if (!isEmptyOfElements)
			nodeDOM.appendChild(document.createTextNode("\n" + getTab(depth)));
	}

	/**
	 * Returns a set of tabulations, equivalent to the given numer
	 * 
	 * @param tabulations
	 *            Number of tabulations
	 */
	private String getTab(int tabulations) {
		String tab = "";
		for (int i = 0; i < tabulations; i++)
			tab += "\t";
		return tab;
	}

	public void cycleCompleted(int timerId, long elapsedTime) {
		// Do nothing
	}

	public void timerStarted(int timerId, long currentTime) {
		// Save the currentTime
		TimedAssessmentRule tRule = this.timedRules.get(new Integer(timerId));
		tRule.ruleStarted(currentTime);
		// System.out.println( "[TIMER STARTED] " + timerId + " - time:
		// "+currentTime );
	}

	public void timerStopped(int timerId, long currentTime) {
		// Get the rule
		TimedAssessmentRule tRule = this.timedRules.get(new Integer(timerId));
		tRule.ruleDone(currentTime);
		// Once the rule has been processed, remove it from the timermanager
		TimerManager.getInstance().deleteTimer(timerId);
		// System.out.println( "[TIMER DONE] " + timerId + " - time:
		// "+currentTime );
	}

	public boolean isEndOfChapterFeedbackDone() {

		if (assessmentProfile != null) {

			if (state == STATE_NONE && assessmentProfile.isShowReportAtEnd()) {

				try {
					state = STATE_STARTED;
					
					Log.d("PASA POR ASSESMENT", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

					int i = 0;
					File reportFile = null;
					String fileName = null;

					if (!ReleaseFolders.reportsFolder().exists()) {
						ReleaseFolders.reportsFolder().mkdirs();
					}

					do {
						i++;
						fileName = "Report_" + i + ".html";
						reportFile = new File(ReleaseFolders.reportsFolder(),
								fileName);
					} while (reportFile.exists());

					reportFile.createNewFile();
					final String reportAbsoluteFile = reportFile
							.getAbsolutePath();

					generateHTMLReport(reportFile.getAbsolutePath(), -5);

					File report = new File(reportAbsoluteFile);

					try {
						FileReader fir = new FileReader(report);
						BufferedReader br = new BufferedReader(fir);
						String line = br.readLine();
						
						String text = "";
						while (line != null) {
							text += line + "\n\r";
							line = br.readLine();
						}
						
						text = checkUriEscaped(text);
						
						lastHTMLReport = text;

						Handler handler = GameThread.getInstance().getHandler();

						Message msg = handler.obtainMessage();
												
						Bundle b = new Bundle();
						b.putString("html", text);
						msg.what = ActivityHandlerMessages.ASSESSMENT;
						msg.setData(b);

						msg.sendToTarget();

					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}


				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				state = STATE_STARTED;
				return false;

			} else if (state == STATE_STARTED) {
				return false;
			} else if (state == STATE_DONE) {
				return true;
			} else
				return true;
		}
		return true;
	}
	
	/** 
	 * ANDROID WEBVIEW : 
	 *  The data must be URI-escaped -- '#', '%', '\', '?' 
	 *  should be replaced by %23, %25, %27, %3f respectively.
	*/

	private String checkUriEscaped(String text) {
		
	//	text = text.replace("#", "%23");
		text = text.replace("%", "%25");
		text = text.replace("\\", "%27");
		text = text.replace("?", "%3f");
		
		return text;
		
	}
	

	public AssessmentProfile getAssessmentProfile() {
		return assessmentProfile;
	}

	/**
	 * @param playerName
	 *            the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setStateDone() {

		state = STATE_DONE;

	}
	
	public String getLastHTMLReport() {
		
		return lastHTMLReport;
		
	}

}