package com.taskadapter.redmineapi;

import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.IssueCategoryFactory;
import com.taskadapter.redmineapi.bean.IssueRelation;
import com.taskadapter.redmineapi.bean.News;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.ProjectFactory;
import com.taskadapter.redmineapi.bean.SavedQuery;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;
import com.taskadapter.redmineapi.bean.VersionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Simple {
	private static final Logger logger = LoggerFactory.getLogger(Simple.class);
	private static final String projectKey = "testid";

	private static final Integer queryId = null; // any

	public static void main(String[] args) {
        RedmineManager mgr = IntegrationTestHelper.createRedmineManager();
		try {
//            getUsersAsNotAdmin(mgr);
			// getIssueWithRelations(mgr);
//			tryCreateIssue(mgr);
			// tryGetIssues(mgr);
			getIssueById(mgr.getIssueManager());
			// tryGetAllIssues(mgr);
			// printCurrentUser(mgr);
			// generateXMLForUser();
			// generateXMLForTimeEntry();
            // getSavedQueries(mgr);
			// getProjects(mgr);
			// tryCreateRelation(mgr);
			// tryGetNews(mgr);
			// getProject(mgr);
			// changeIssueStatus(mgr);
			// getVersion(mgr);
			// getStatuses(mgr);
			// tryUpload(mgr);
//			tryGetRoles(mgr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private static void getUsersAsNotAdmin(RedmineManager mgr) throws RedmineException {
        System.out.println("Users: " + mgr.getUserManager().getUsers());
    }

    @SuppressWarnings("unused")
	private static void tryUpload(RedmineManager mgr, IssueManager issueManager, AttachmentManager attachmentManager) throws RedmineException,
			IOException {
		final byte[] content = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		final Attachment attach1 = attachmentManager.uploadAttachment("test.bin",
				"application/ternary", content);
		final Project tmpProject = ProjectFactory.create("Upload project", "uploadtmpproject");
		final Project project = mgr.getProjectManager().createProject(tmpProject);
		final Issue testIssue = new Issue();
		testIssue.setSubject("This is upload ticket!");
		testIssue.addAttachment(attach1);
		testIssue.setProjectId(project.getId());
		try {
			final Issue createdIssue = issueManager.createIssue(testIssue);
			try {
				System.out.println(createdIssue.getAttachments());
			} finally {
				issueManager.deleteIssue(createdIssue.getId());
			}
		} finally {
			mgr.getProjectManager().deleteProject(project.getIdentifier());
		}
	}

	@SuppressWarnings("unused")
	private static void getVersion(ProjectManager mgr) throws RedmineException {
		// see Redmine bug http://www.redmine.org/issues/10241
		Version version = mgr.getVersionById(294);
		System.out.println(version);
	}

	@SuppressWarnings("unused")
	private static void changeIssueStatus(IssueManager issueManager)
			throws RedmineException {
		Issue issue = issueManager.getIssueById(1771);
		issue.setSubject("new");
		issueManager.update(issue);
	}

	@SuppressWarnings("unused")
	private static void getProject(RedmineManager mgr) throws RedmineException {
		Project test = mgr.getProjectManager().getProjectByKey("test");
		System.out.println(test);
	}

	@SuppressWarnings("unused")
	private static void getStatuses(IssueManager mgr) throws RedmineException {
		mgr.getStatuses();
	}

	@SuppressWarnings("unused")
	private static void tryGetNews(ProjectManager mgr) throws RedmineException {
		List<News> news = mgr.getNews(null);
		news.forEach(System.out::println);
	}

	@SuppressWarnings("unused")
	private static void tryCreateRelation(IssueManager issueManager)
			throws RedmineException {
		IssueRelation r = issueManager.createRelation(49, 50,
				IssueRelation.TYPE.precedes.toString());
		logger.debug("Created relation " + r);
	}

	@SuppressWarnings("unused")
	private static void getProjects(RedmineManager mgr) throws RedmineException {
		List<Project> projects = mgr.getProjectManager().getProjects();
		logger.debug("Retrieved projects " + projects);

	}

	@SuppressWarnings("unused")
	private static void getSavedQueries(IssueManager mgr)
			throws RedmineException {
		List<SavedQuery> savedQueries = mgr.getSavedQueries("test");
		System.out.println(savedQueries.size());
		logger.debug("Retrieved queries " + savedQueries);

	}

	@SuppressWarnings("unused")
	private static void getIssueWithRelations(IssueManager issueManager)
			throws RedmineException {
		Issue issue = issueManager.getIssueById(22751, Include.relations);
		Collection<IssueRelation> r = issue.getRelations();
		logger.debug("Retrieved relations " + r);
	}

	@SuppressWarnings("unused")
	private static void tryCreateIssue(RedmineManager manager)
			throws RedmineException {
		Issue issue = new Issue();
		issue.setSubject("test123");
		final Version ver = VersionFactory.create(512);
		issue.setTargetVersion(ver);
		final IssueCategory cat = IssueCategoryFactory.create(673);
		issue.setCategory(cat);

        ProjectManager projectManager = manager.getProjectManager();
        Project projectByKey = projectManager.getProjectByKey("testid");
        issue.setProjectId(projectByKey.getId());
        manager.getIssueManager().createIssue(issue);
	}

	@SuppressWarnings("unused")
	private static void tryGetIssues(IssueManager issueManager) throws Exception {
		List<Issue> issues = issueManager.getIssuesBySummary(projectKey, "Russian");
		for (Issue issue : issues) {
			logger.debug(issue.toString());
		}
	}

	private static void getIssueById(IssueManager issueManager) throws Exception {
		Issue issue = issueManager.getIssueById(12275, Include.journals, Include.relations, Include.attachments);
		System.out.println(issue.getJournals());
	}

	@SuppressWarnings("unused")
	private static void tryGetAllIssues(IssueManager issueManager) throws Exception {
		List<Issue> issues = issueManager.getIssues(projectKey, null);
		for (Issue issue : issues) {
			logger.debug(issue.toString());
		}
	}

	@SuppressWarnings("unused")
	private static void printCurrentUser(UserManager mgr) throws Exception {
		User currentUser = mgr.getCurrentUser();
		logger.debug("user=" + currentUser.getMail());

		currentUser.setMail("ne@com123.com");
		mgr.update(currentUser);
		logger.debug("updated user");

		User currentUser2 = mgr.getCurrentUser();
		logger.debug("updated user's mail: " + currentUser2.getMail());

	}

	private static void tryGetRoles(RedmineManager mgr) throws Exception {
		System.out.println(mgr.getUserManager().getRoles());
	}
}
